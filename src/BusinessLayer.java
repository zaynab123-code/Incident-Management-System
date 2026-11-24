// =============================================================================
// FILE: BusinessLayer.java
// LAYER: Business Logic Layer
// =============================================================================

import java.util.*;

// =============================================================================
// OBSERVER PATTERN
// =============================================================================

interface SystemObserver {
    void onIncidentUpdated(Incident incident);
    void onMapUpdated(MapLocation location, IncidentStatus status);
    void onDisputeRaised(Incident incident);
}

class ObservableSystem {
    private final List<SystemObserver> observers = new ArrayList<>();

    public void addObserver(SystemObserver o)    { if (!observers.contains(o)) observers.add(o); }
    public void removeObserver(SystemObserver o) { observers.remove(o); }

    protected void notifyIncidentUpdated(Incident i) {
        for (SystemObserver o : new ArrayList<>(observers)) o.onIncidentUpdated(i);
    }
    protected void notifyMapUpdated(MapLocation l, IncidentStatus s) {
        for (SystemObserver o : new ArrayList<>(observers)) o.onMapUpdated(l, s);
    }
    protected void notifyDisputeRaised(Incident i) {
        for (SystemObserver o : new ArrayList<>(observers)) o.onDisputeRaised(i);
    }
}

// =============================================================================
// MAP SERVICE
// =============================================================================

class MapService extends ObservableSystem {
    private final Map<MapLocation, MapPoint> points = new LinkedHashMap<>();

    private static final double[][] REL = {
        {0.22, 0.30},  // CITY_CENTER
        {0.78, 0.30},  // GREEN_VALLEY
        {0.22, 0.70},  // WESTERN_BRIDGE
        {0.78, 0.70},  // SEONS_ALLEY
    };

    public MapService() {
        MapLocation[] locs = MapLocation.values();
        for (int i = 0; i < locs.length; i++)
            points.put(locs[i], new MapPoint(locs[i], REL[i][0], REL[i][1]));
    }

    public void recalculate(MapLocation loc, List<Incident> incidents) {
        IncidentStatus worst = IncidentStatus.RESOLVED;
        outer:
        for (Incident inc : incidents) {
            if (inc.getStatus() == IncidentStatus.RESOLVED) continue;
            switch (inc.getStatus()) {
                case REPORTED:     worst = IncidentStatus.REPORTED; break outer;
                case UNDER_REVIEW:
                    if (worst != IncidentStatus.REPORTED) worst = IncidentStatus.UNDER_REVIEW; break;
                case IN_PROGRESS:
                    if (worst != IncidentStatus.REPORTED && worst != IncidentStatus.UNDER_REVIEW)
                        worst = IncidentStatus.IN_PROGRESS; break;
                case TEAM_ASSIGNED:
                    if (worst == IncidentStatus.RESOLVED || worst == IncidentStatus.PENDING)
                        worst = IncidentStatus.TEAM_ASSIGNED; break;
                case PENDING:
                    if (worst == IncidentStatus.RESOLVED) worst = IncidentStatus.PENDING; break;
                default: break;
            }
        }
        MapPoint pt = points.get(loc);
        if (pt != null) { pt.setCurrentStatus(worst); notifyMapUpdated(loc, worst); }
    }

    public void setStatus(MapLocation loc, IncidentStatus status) {
        MapPoint pt = points.get(loc);
        if (pt != null) { pt.setCurrentStatus(status); notifyMapUpdated(loc, status); }
    }

    public Collection<MapPoint> getAllPoints() { return points.values(); }
    public MapPoint getPoint(MapLocation loc)  { return points.get(loc); }

    public void syncMapOnStartup() {
        IncidentRepository repo = new IncidentRepository();
        for (MapLocation loc : MapLocation.values())
            recalculate(loc, repo.findByLocation(loc));
    }
}

// =============================================================================
// AUTH CONTROLLER
// =============================================================================

class AuthController {
    private final AdminRepository   adminRepo   = new AdminRepository();
    private final CitizenRepository citizenRepo = new CitizenRepository();

    public Citizen registerCitizen(String name, String email, boolean accepted) {
        ValidationResult nr = InputValidator.validateName(name);
        if (!nr.isValid()) throw new IllegalArgumentException(nr.getMessage());
        ValidationResult er = InputValidator.validateEmail(email);
        if (!er.isValid()) throw new IllegalArgumentException(er.getMessage());
        if (!accepted) throw new IllegalStateException("You must accept the legal declaration.");

        Citizen c = new Citizen(name.trim(), email.trim().toLowerCase(), true);
        return citizenRepo.save(c);
    }

    public Admin loginAdmin(String name, String password, String workId) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
        if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password cannot be empty.");
        if (workId == null || workId.trim().isEmpty()) throw new IllegalArgumentException("Work ID cannot be empty.");

        Admin admin = adminRepo.authenticate(name, password, workId);
        if (admin == null) throw new IllegalArgumentException("Invalid credentials.");
        return admin;
    }

    /** Check whether this citizen has already accepted the declaration (persisted) */
    public boolean hasDeclarationAccepted(String email) {
        return citizenRepo.hasDeclarationAccepted(email);
    }

    /** Persist declaration acceptance */
    public void persistDeclarationAcceptance(String email) {
        citizenRepo.setDeclarationAccepted(email);
    }
}

// =============================================================================
// INCIDENT CONTROLLER
// =============================================================================

class IncidentController extends ObservableSystem {
    private final IncidentRepository incidentRepo = new IncidentRepository();
    private final TeamRepository     teamRepo     = new TeamRepository();
    private final CitizenRepository  citizenRepo  = new CitizenRepository();
    private final DisputeRepository  disputeRepo  = new DisputeRepository();
    private final MapService         mapService;

    public IncidentController(MapService mapService) { this.mapService = mapService; }

    // ── REPORT ────────────────────────────────────────────────────────────────
    public Incident reportIncident(Citizen citizen, IncidentType type,
                                   MapLocation location, String description) {
        ValidationResult vr = InputValidator.validateDescription(description);
        if (!vr.isValid()) throw new IllegalArgumentException(vr.getMessage());

        Incident existing = incidentRepo.findDuplicate(type, location);
        if (existing != null) {
            existing.addLinkedCitizen(citizen);
            incidentRepo.linkCitizen(existing.getIncidentId(), citizen);
            notifyIncidentUpdated(existing);
            return existing;
        }

        String id = incidentRepo.generateId();
        Incident inc = new Incident(id, description, type, location);
        incidentRepo.create(inc, citizen);
        recalcMap(location);
        notifyIncidentUpdated(inc);
        return inc;
    }

    // ── ASSIGN TEAM ───────────────────────────────────────────────────────────
    public void assignTeam(String incidentId, String teamName) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        if (inc.getStatus() == IncidentStatus.RESOLVED)
            throw new IllegalStateException("Cannot assign to resolved incident.");

        ResponseTeam team = teamRepo.findByName(teamName);
        if (team == null)        throw new IllegalArgumentException("Team not found: " + teamName);
        if (!team.isAvailable()) throw new IllegalStateException("Team " + teamName + " is not available.");
        if (team.getCategory() != inc.getType())
            throw new IllegalArgumentException("Team type mismatch.");

        if (inc.getAssignedTeam() != null)
            teamRepo.setAvailability(inc.getAssignedTeam().getTeamName(), true);

        inc.setAssignedTeam(team);
        inc.setStatus(IncidentStatus.TEAM_ASSIGNED);
        teamRepo.setAvailability(teamName, false);
        incidentRepo.update(inc);
        recalcMap(inc.getLocation());
        notifyIncidentUpdated(inc);
    }

    public void markPending(String incidentId) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        inc.setStatus(IncidentStatus.PENDING);
        incidentRepo.update(inc); recalcMap(inc.getLocation()); notifyIncidentUpdated(inc);
    }

    // ── TEAM: MARK ARRIVAL (only assigned team) ───────────────────────────────
    public void markArrival(String incidentId, String teamName) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        if (!inc.isAssignedTeam(teamName))
            throw new IllegalStateException("Only the assigned team can mark arrival.");
        if (inc.getStatus() != IncidentStatus.TEAM_ASSIGNED)
            throw new IllegalStateException("Team must be in TEAM_ASSIGNED status.");
        inc.setArrivedNotified(true);
        incidentRepo.update(inc); notifyIncidentUpdated(inc);
    }

    // ── CITIZEN: CONFIRM ARRIVAL (only first reporter) ────────────────────────
    public void citizenConfirmArrival(String incidentId, Citizen citizen) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        if (!inc.isFirstReporter(citizen.getEmail()))
            throw new IllegalStateException("Only the first reporter can confirm arrival.");
        if (!inc.isArrivedNotified())
            throw new IllegalStateException("Team has not marked arrival yet.");
        inc.setStatus(IncidentStatus.IN_PROGRESS);
        incidentRepo.update(inc); recalcMap(inc.getLocation()); notifyIncidentUpdated(inc);
    }

    // ── TEAM: MARK RESOLVED (only assigned team) ──────────────────────────────
    public void teamMarkResolved(String incidentId, String teamName) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        if (!inc.isAssignedTeam(teamName))
            throw new IllegalStateException("Only the assigned team can mark resolution.");
        if (inc.getStatus() != IncidentStatus.IN_PROGRESS)
            throw new IllegalStateException("Incident must be IN_PROGRESS.");
        inc.setTeamMarkedResolved(true);
        incidentRepo.update(inc); notifyIncidentUpdated(inc);
    }

    // ── CITIZEN: CONFIRM RESOLUTION (only first reporter) ─────────────────────
    public void citizenConfirmResolution(String incidentId, Citizen citizen) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        if (!inc.isFirstReporter(citizen.getEmail()))
            throw new IllegalStateException("Only the first reporter can confirm resolution.");
        if (!inc.isTeamMarkedResolved())
            throw new IllegalStateException("Team has not marked this incident as resolved.");
        if (inc.getAssignedTeam() != null)
            teamRepo.setAvailability(inc.getAssignedTeam().getTeamName(), true);
        inc.setStatus(IncidentStatus.RESOLVED);
        incidentRepo.update(inc);
        // ADD to existing points — never overwrite
        citizenRepo.addRewardPoints(citizen.getEmail(), 10);
        recalcMap(inc.getLocation()); notifyIncidentUpdated(inc);
    }

    // ── ADMIN: FORCE RESOLVE ──────────────────────────────────────────────────
    public void resolveIncident(String incidentId) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        if (inc.getStatus() == IncidentStatus.RESOLVED) throw new IllegalStateException("Already resolved.");
        if (inc.getAssignedTeam() != null)
            teamRepo.setAvailability(inc.getAssignedTeam().getTeamName(), true);
        inc.setStatus(IncidentStatus.RESOLVED);
        incidentRepo.update(inc); recalcMap(inc.getLocation()); notifyIncidentUpdated(inc);
    }

    public void updateStatus(String incidentId, IncidentStatus newStatus) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        if (newStatus == IncidentStatus.RESOLVED && inc.getAssignedTeam() != null)
            teamRepo.setAvailability(inc.getAssignedTeam().getTeamName(), true);
        inc.setStatus(newStatus);
        incidentRepo.update(inc); recalcMap(inc.getLocation()); notifyIncidentUpdated(inc);
    }

    // ── ACTION-BASED DISPUTE: CITIZEN ─────────────────────────────────────────
    // Raised DURING Confirm Arrival or Confirm Resolution by the FIRST reporter only
    public Dispute raiseCitizenDispute(String incidentId, Citizen citizen,
                                       CitizenDisputeType disputeType, String action) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");

        // Access control: only first reporter
        if (!inc.isFirstReporter(citizen.getEmail()))
            throw new IllegalStateException("Only the first reporter can raise a citizen dispute.");

        // Validate action context
        if (!action.equals("CONFIRM_ARRIVAL") && !action.equals("CONFIRM_RESOLUTION"))
            throw new IllegalStateException("Citizen disputes can only be raised during Confirm Arrival or Confirm Resolution.");

        ValidationResult vr = InputValidator.validateDisputeNote(disputeType.getLabel());
        if (!vr.isValid()) throw new IllegalArgumentException(vr.getMessage());

        String disputeId = DBHandler.getInstance().generateDisputeId();
        Dispute d = new Dispute(disputeId, incidentId, citizen.getEmail(),
            DisputeRole.CITIZEN, disputeType.getLabel(), action);
        disputeRepo.save(d);

        // Mark incident under review
        inc.setDisputeRaised(true);
        inc.setDisputeResolved(false);
        inc.setDisputeNote("[CITIZEN] " + disputeType.getLabel());
        inc.setStatus(IncidentStatus.UNDER_REVIEW);
        incidentRepo.update(inc); recalcMap(inc.getLocation());
        notifyDisputeRaised(inc);
        return d;
    }

    // ── ACTION-BASED DISPUTE: TEAM ─────────────────────────────────────────────
    // Raised DURING Mark Arrival or Mark Resolution by the ASSIGNED team only
    public Dispute raiseTeamDispute(String incidentId, String teamName,
                                    TeamDisputeType disputeType, String action) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");

        // Access control: only assigned team
        if (!inc.isAssignedTeam(teamName))
            throw new IllegalStateException("Only the assigned team can raise a team dispute.");

        // Validate action context
        if (!action.equals("MARK_ARRIVAL") && !action.equals("MARK_RESOLUTION"))
            throw new IllegalStateException("Team disputes can only be raised during Mark Arrival or Mark Resolution.");

        String disputeId = DBHandler.getInstance().generateDisputeId();
        Dispute d = new Dispute(disputeId, incidentId, teamName,
            DisputeRole.TEAM, disputeType.getLabel(), action);
        disputeRepo.save(d);

        inc.setDisputeRaised(true);
        inc.setDisputeResolved(false);
        inc.setDisputeNote("[TEAM] " + disputeType.getLabel());
        inc.setStatus(IncidentStatus.UNDER_REVIEW);
        incidentRepo.update(inc); recalcMap(inc.getLocation());
        notifyDisputeRaised(inc);
        return d;
    }

    // ── ADMIN: RESOLVE DISPUTE ────────────────────────────────────────────────
    public void resolveDispute(String incidentId) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        if (!inc.isDisputeRaised()) throw new IllegalStateException("No dispute raised.");
        inc.setDisputeResolved(true);
        incidentRepo.update(inc); notifyIncidentUpdated(inc);
    }

    public void markDisputeInvalid(String incidentId) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        if (!inc.isDisputeRaised()) throw new IllegalStateException("No dispute raised.");
        if (!inc.getLinkedCitizens().isEmpty())
            citizenRepo.setBlocked(inc.getLinkedCitizens().get(0).getEmail(), true);
        inc.setDisputeResolved(true);
        inc.setStatus(IncidentStatus.RESOLVED);
        if (inc.getAssignedTeam() != null)
            teamRepo.setAvailability(inc.getAssignedTeam().getTeamName(), true);
        incidentRepo.update(inc); recalcMap(inc.getLocation()); notifyIncidentUpdated(inc);
    }

    // ── REWARD / BLOCKED ──────────────────────────────────────────────────────
    public int     getRewardPoints(String email)      { return citizenRepo.getRewardPoints(email); }
    public boolean isCitizenBlocked(String email)     { return citizenRepo.isBlocked(email); }

    // ── QUERIES ───────────────────────────────────────────────────────────────
    public List<Incident>  getAllIncidents()             { return incidentRepo.findAll(); }
    public Incident        getById(String id)            { return incidentRepo.findById(id); }
    public List<Incident>  getCitizenIncidents(String e) { return incidentRepo.findByCitizen(e); }
    public List<Incident>  getOpenDisputes()             { return incidentRepo.findOpenDisputes(); }
    public List<Incident>  getTeamIncidents(String name) { return incidentRepo.findByTeamName(name); }
    public List<Dispute>   getOpenDisputesList()         { return new DisputeRepository().findOpen(); }

    public ResponseTeam autoDispatch(String incidentId) {
        Incident inc = incidentRepo.findById(incidentId);
        if (inc == null) throw new IllegalArgumentException("Incident not found.");
        List<ResponseTeam> avail = teamRepo.findAvailableByType(inc.getType());
        if (avail.isEmpty()) throw new IllegalStateException("No available team for type: " + inc.getType());
        ResponseTeam team = avail.get(0);
        assignTeam(incidentId, team.getTeamName());
        return team;
    }

    public List<ResponseTeam> getAllTeams()                     { return teamRepo.findAll(); }
    public List<ResponseTeam> getAvailableTeams(IncidentType t) { return teamRepo.findAvailableByType(t); }

    private void recalcMap(MapLocation loc) {
        mapService.recalculate(loc, incidentRepo.findByLocation(loc));
    }

    public void syncMapOnStartup() {
        for (MapLocation loc : MapLocation.values()) recalcMap(loc);
    }
}

// =============================================================================
// SYSTEM FACADE
// =============================================================================

class SystemFacade {
    private static SystemFacade instance;

    private final IncidentController incidentCtrl;
    private final AuthController     authCtrl;
    private final MapService         mapService;

    private SystemFacade() {
        mapService   = new MapService();
        incidentCtrl = new IncidentController(mapService);
        authCtrl     = new AuthController();
        incidentCtrl.syncMapOnStartup();
    }

    public static synchronized SystemFacade getInstance() {
        if (instance == null) instance = new SystemFacade();
        return instance;
    }

    // ── AUTH ──────────────────────────────────────────────────────────────────
    public Citizen registerCitizen(String name, String email, boolean decl) {
        return authCtrl.registerCitizen(name, email, decl);
    }
    public Admin loginAdmin(String name, String pw, String wid) {
        return authCtrl.loginAdmin(name, pw, wid);
    }
    /** Check if declaration was already accepted (persisted) for this email */
    public boolean hasDeclarationAccepted(String email) {
        return authCtrl.hasDeclarationAccepted(email);
    }
    /** Persist declaration acceptance to DB */
    public void persistDeclarationAcceptance(String email) {
        authCtrl.persistDeclarationAcceptance(email);
    }

    // ── INCIDENTS ─────────────────────────────────────────────────────────────
    public Incident reportIncident(Citizen c, IncidentType t, MapLocation l, String d) {
        return incidentCtrl.reportIncident(c, t, l, d);
    }
    public void assignTeam(String incId, String teamName)         { incidentCtrl.assignTeam(incId, teamName); }
    public void markPending(String incId)                         { incidentCtrl.markPending(incId); }
    public ResponseTeam autoDispatch(String incId)                { return incidentCtrl.autoDispatch(incId); }

    /** Team marks arrival — requires teamName for permission check */
    public void markArrival(String incId, String teamName)        { incidentCtrl.markArrival(incId, teamName); }

    /** Citizen confirms arrival — only first reporter */
    public void citizenConfirmArrival(String incId, Citizen c)    { incidentCtrl.citizenConfirmArrival(incId, c); }

    /** Team marks resolved — requires teamName for permission check */
    public void teamMarkResolved(String incId, String teamName)   { incidentCtrl.teamMarkResolved(incId, teamName); }

    /** Citizen confirms resolution — only first reporter; adds points */
    public void citizenConfirmResolution(String incId, Citizen c) { incidentCtrl.citizenConfirmResolution(incId, c); }

    public void resolveIncident(String incId)                     { incidentCtrl.resolveIncident(incId); }
    public void updateIncidentStatus(String incId, IncidentStatus s){ incidentCtrl.updateStatus(incId, s); }

    // ── ACTION-BASED DISPUTES ─────────────────────────────────────────────────
    public Dispute raiseCitizenDispute(String incId, Citizen citizen,
                                       CitizenDisputeType type, String action) {
        return incidentCtrl.raiseCitizenDispute(incId, citizen, type, action);
    }
    public Dispute raiseTeamDispute(String incId, String teamName,
                                    TeamDisputeType type, String action) {
        return incidentCtrl.raiseTeamDispute(incId, teamName, type, action);
    }

    public void resolveDispute(String incId)      { incidentCtrl.resolveDispute(incId); }
    public void markDisputeInvalid(String incId)  { incidentCtrl.markDisputeInvalid(incId); }

    public int     getRewardPoints(String email)      { return incidentCtrl.getRewardPoints(email); }
    public boolean isCitizenBlocked(String email)     { return incidentCtrl.isCitizenBlocked(email); }

    // ── QUERIES ───────────────────────────────────────────────────────────────
    public List<Incident>     getAllIncidents()              { return incidentCtrl.getAllIncidents(); }
    public Incident           getIncidentById(String id)    { return incidentCtrl.getById(id); }
    public List<Incident>     getCitizenIncidents(String e) { return incidentCtrl.getCitizenIncidents(e); }
    public List<Incident>     getOpenDisputes()             { return incidentCtrl.getOpenDisputes(); }
    public List<Incident>     getTeamIncidents(String name) { return incidentCtrl.getTeamIncidents(name); }
    public List<ResponseTeam> getAllTeams()                  { return incidentCtrl.getAllTeams(); }
    public List<ResponseTeam> getAvailableTeams(IncidentType t){ return incidentCtrl.getAvailableTeams(t); }

    // ── MAP ───────────────────────────────────────────────────────────────────
    public MapService           getMapService()   { return mapService; }
    public Collection<MapPoint> getAllMapPoints()  { return mapService.getAllPoints(); }
    public void syncMapOnStartup() {
        incidentCtrl.syncMapOnStartup(); mapService.syncMapOnStartup();
    }

    // ── OBSERVERS ─────────────────────────────────────────────────────────────
    public void addObserver(SystemObserver o)    { incidentCtrl.addObserver(o); mapService.addObserver(o); }
    public void removeObserver(SystemObserver o) { incidentCtrl.removeObserver(o); mapService.removeObserver(o); }

    // ── VALIDATION ────────────────────────────────────────────────────────────
    public ValidationResult validateName(String n)  { return InputValidator.validateName(n); }
    public ValidationResult validateEmail(String e) { return InputValidator.validateEmail(e); }

    // ── DB STATUS ─────────────────────────────────────────────────────────────
    public boolean isDbConnected() { return DBHandler.getInstance().isConnected(); }
}
