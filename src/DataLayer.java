// =============================================================================
// FILE: DataLayer.java
// LAYER: Model + Data Access Layer
// Contains every type that BusinessLayer.java and UILayer.java depend on.
// No public classes here, so the file name is free of the
// "public type must be defined in its own file" rule.
// =============================================================================

import java.util.*;
import java.util.regex.Pattern;

// =============================================================================
// ENUMS
// =============================================================================

enum IncidentType { FIRE, THEFT, ACCIDENT }

enum MapLocation { CITY_CENTER, GREEN_VALLEY, WESTERN_BRIDGE, SEONS_ALLEY }

enum IncidentStatus { REPORTED, PENDING, TEAM_ASSIGNED, IN_PROGRESS, UNDER_REVIEW, RESOLVED }

enum DisputeRole { CITIZEN, TEAM }

enum CitizenDisputeType {
    TEAM_NOT_ARRIVED("Team did not arrive"),
    WRONG_TEAM("Wrong team arrived"),
    TEAM_NOT_PROFESSIONAL("Team was unprofessional"),
    ISSUE_NOT_RESOLVED("Issue is not resolved"),
    INCOMPLETE_WORK("Work was incomplete");

    private final String label;
    CitizenDisputeType(String label) { this.label = label; }
    public String getLabel() { return label; }
    @Override public String toString() { return label; }
}

enum TeamDisputeType {
    FALSE_REPORT("Report appears to be false"),
    WRONG_LOCATION("Incident location is wrong"),
    NO_ISSUE_FOUND("No issue found at location"),
    CITIZEN_UNREACHABLE("Reporter is unreachable");

    private final String label;
    TeamDisputeType(String label) { this.label = label; }
    public String getLabel() { return label; }
    @Override public String toString() { return label; }
}

// =============================================================================
// MODEL CLASSES
// =============================================================================

class ValidationResult {
    private final boolean valid;
    private final String message;
    ValidationResult(boolean valid, String message) { this.valid = valid; this.message = message; }
    public boolean isValid()     { return valid; }
    public String  getMessage()  { return message; }
    static ValidationResult ok() { return new ValidationResult(true, ""); }
    static ValidationResult fail(String m) { return new ValidationResult(false, m); }
}

class Citizen {
    private final String name;
    private final String email;
    private boolean declarationAccepted;

    public Citizen(String name, String email, boolean declarationAccepted) {
        this.name = name;
        this.email = email;
        this.declarationAccepted = declarationAccepted;
    }
    public String  getName()  { return name; }
    public String  getEmail() { return email; }
    public boolean isDeclarationAccepted() { return declarationAccepted; }
    public void setDeclarationAccepted(boolean b) { this.declarationAccepted = b; }
}

class Admin {
    private final String name;
    private final String workId;
    public Admin(String name, String workId) { this.name = name; this.workId = workId; }
    public String getName()   { return name; }
    public String getWorkId() { return workId; }
}

class ResponseTeam {
    private final String teamName;
    private final String teamType;
    private final IncidentType category;
    private final String teamLeader;
    private final String vehicleNumber;
    private boolean available = true;

    public ResponseTeam(String teamName, String teamType, IncidentType category,
                        String teamLeader, String vehicleNumber) {
        this.teamName = teamName;
        this.teamType = teamType;
        this.category = category;
        this.teamLeader = teamLeader;
        this.vehicleNumber = vehicleNumber;
    }
    public String       getTeamName()      { return teamName; }
    public String       getTeamType()      { return teamType; }
    public IncidentType getCategory()      { return category; }
    public String       getTeamLeader()    { return teamLeader; }
    public String       getVehicleNumber() { return vehicleNumber; }
    public boolean      isAvailable()      { return available; }
    public void         setAvailable(boolean a) { this.available = a; }
}

class Incident {
    private final String incidentId;
    private final String description;
    private final IncidentType type;
    private final MapLocation location;
    private final Date timestamp = new Date();
    private IncidentStatus status = IncidentStatus.REPORTED;
    private ResponseTeam assignedTeam;
    private final List<Citizen> linkedCitizens = new ArrayList<>();

    private boolean arrivedNotified;
    private boolean teamMarkedResolved;
    private boolean disputeRaised;
    private boolean disputeResolved;
    private String  disputeNote;

    public Incident(String incidentId, String description, IncidentType type, MapLocation location) {
        this.incidentId = incidentId;
        this.description = description;
        this.type = type;
        this.location = location;
    }

    public String        getIncidentId() { return incidentId; }
    public String        getDescription(){ return description; }
    public IncidentType  getType()       { return type; }
    public MapLocation   getLocation()   { return location; }
    public Date          getTimestamp()  { return timestamp; }
    public String getFormattedTimestamp() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp);
    }

    public IncidentStatus getStatus()                { return status; }
    public void setStatus(IncidentStatus s)          { this.status = s; }
    public ResponseTeam getAssignedTeam()            { return assignedTeam; }
    public void setAssignedTeam(ResponseTeam t)      { this.assignedTeam = t; }

    public boolean isAssignedTeam(String teamName) {
        return assignedTeam != null && assignedTeam.getTeamName().equals(teamName);
    }

    // ── citizens ──────────────────────────────────────────────────────────────
    public List<Citizen> getLinkedCitizens() { return linkedCitizens; }

    public void addLinkedCitizen(Citizen c) {
        if (c == null) return;
        for (Citizen x : linkedCitizens)
            if (x.getEmail().equalsIgnoreCase(c.getEmail())) return;
        linkedCitizens.add(c);
    }
    public boolean isFirstReporter(String email) {
        return !linkedCitizens.isEmpty() && email != null
            && linkedCitizens.get(0).getEmail().equalsIgnoreCase(email);
    }
    /** 1-based reporter number, or 0 if the citizen is not linked. */
    public int getReporterNumber(String email) {
        for (int i = 0; i < linkedCitizens.size(); i++)
            if (linkedCitizens.get(i).getEmail().equalsIgnoreCase(email)) return i + 1;
        return 0;
    }

    // ── workflow flags ────────────────────────────────────────────────────────
    public boolean isArrivedNotified()               { return arrivedNotified; }
    public void setArrivedNotified(boolean b)        { this.arrivedNotified = b; }
    public boolean isTeamMarkedResolved()            { return teamMarkedResolved; }
    public void setTeamMarkedResolved(boolean b)     { this.teamMarkedResolved = b; }

    // ── dispute ───────────────────────────────────────────────────────────────
    public boolean isDisputeRaised()                 { return disputeRaised; }
    public void setDisputeRaised(boolean b)          { this.disputeRaised = b; }
    public boolean isDisputeResolved()               { return disputeResolved; }
    public void setDisputeResolved(boolean b)        { this.disputeResolved = b; }
    public String getDisputeNote()                   { return disputeNote; }
    public void setDisputeNote(String n)             { this.disputeNote = n; }
}

class Dispute {
    private final String id;
    private final String incidentId;
    private final String raisedBy;
    private final DisputeRole role;
    private final String reason;
    private final String action;
    private boolean resolved;

    public Dispute(String id, String incidentId, String raisedBy,
                   DisputeRole role, String reason, String action) {
        this.id = id;
        this.incidentId = incidentId;
        this.raisedBy = raisedBy;
        this.role = role;
        this.reason = reason;
        this.action = action;
    }
    public String      getId()         { return id; }
    public String      getIncidentId() { return incidentId; }
    public String      getRaisedBy()   { return raisedBy; }
    public DisputeRole getRole()       { return role; }
    public String      getReason()     { return reason; }
    public String      getAction()     { return action; }
    public boolean     isResolved()    { return resolved; }
    public void        setResolved(boolean r) { this.resolved = r; }
}

class MapPoint {
    private final MapLocation location;
    private final double rx, ry;
    private IncidentStatus currentStatus = IncidentStatus.RESOLVED;

    public MapPoint(MapLocation location, double rx, double ry) {
        this.location = location;
        this.rx = rx;
        this.ry = ry;
    }
    public MapLocation    getLocation()      { return location; }
    public double         getRX()            { return rx; }
    public double         getRY()            { return ry; }
    public IncidentStatus getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(IncidentStatus s) { this.currentStatus = s; }

    /** Colour used by MapCanvas (java.awt.Color is converted to JavaFX there). */
    public java.awt.Color getColor() {
        switch (currentStatus) {
            case REPORTED:      return new java.awt.Color(220, 30, 30);
            case PENDING:       return new java.awt.Color(120, 120, 120);
            case TEAM_ASSIGNED: return new java.awt.Color(220, 200, 20);
            case IN_PROGRESS:   return new java.awt.Color(230, 140, 20);
            case UNDER_REVIEW:  return new java.awt.Color(140, 30, 220);
            case RESOLVED:
            default:            return new java.awt.Color(34, 197, 94);
        }
    }
}

// =============================================================================
// INPUT VALIDATION
// =============================================================================

class InputValidator {
    private static final Pattern NAME  = Pattern.compile("^[\\p{L}][\\p{L} .'\\-]{1,49}$");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$");

    static ValidationResult validateName(String n) {
        if (n == null || n.trim().isEmpty()) return ValidationResult.fail("Name cannot be empty.");
        if (!NAME.matcher(n.trim()).matches())
            return ValidationResult.fail("Name must be 2-50 letters (spaces, . ' - allowed).");
        return ValidationResult.ok();
    }
    static ValidationResult validateEmail(String e) {
        if (e == null || e.trim().isEmpty()) return ValidationResult.fail("Email cannot be empty.");
        if (!EMAIL.matcher(e.trim()).matches()) return ValidationResult.fail("Enter a valid email address.");
        return ValidationResult.ok();
    }
    static ValidationResult validateDescription(String d) {
        if (d == null || d.trim().length() < 10)
            return ValidationResult.fail("Description must be at least 10 characters.");
        if (d.trim().length() > 500)
            return ValidationResult.fail("Description must be at most 500 characters.");
        return ValidationResult.ok();
    }
    static ValidationResult validateDisputeNote(String n) {
        if (n == null || n.trim().isEmpty()) return ValidationResult.fail("Dispute reason cannot be empty.");
        return ValidationResult.ok();
    }
}

// =============================================================================
// DB HANDLER
// Single access point for storage. Currently an in-memory store (isConnected()
// returns false => the landing page shows "in-memory mode").
// To use your shared/server database, replace the static collections below
// with JDBC calls and set `connected = true` once the connection succeeds.
// Repositories only talk to this class, so nothing else needs to change.
// =============================================================================

class DBHandler {
    private static DBHandler instance;

    final Map<String, Incident>      incidents = new LinkedHashMap<>();
    final Map<String, Citizen>       citizens  = new LinkedHashMap<>();
    final Map<String, Integer>       rewards   = new HashMap<>();
    final Set<String>                blocked   = new HashSet<>();
    final Set<String>                declared  = new HashSet<>();
    final List<ResponseTeam>         teams     = new ArrayList<>();
    final List<String[]>             admins    = new ArrayList<>();   // {name, password, workId}
    final List<Dispute>              disputes  = new ArrayList<>();

    private int incidentSeq = 0;
    private int disputeSeq  = 0;
    private boolean connected = false;

    private DBHandler() { seed(); }

    public static synchronized DBHandler getInstance() {
        if (instance == null) instance = new DBHandler();
        return instance;
    }

    public boolean isConnected() { return connected; }

    synchronized String nextIncidentId() { return String.format("INC-%03d", ++incidentSeq); }
    public synchronized String generateDisputeId() { return String.format("DSP-%03d", ++disputeSeq); }

    private void seed() {
        admins.add(new String[]{"Admin One", "admin123", "ADM001"});
        admins.add(new String[]{"Admin Two", "admin456", "ADM002"});

        teams.add(new ResponseTeam("Fire Team Alpha",   "Fire & Rescue",     IncidentType.FIRE,     "Capt. Hassan Raza",  "FR-101"));
        teams.add(new ResponseTeam("Fire Team Bravo",   "Fire & Rescue",     IncidentType.FIRE,     "Capt. Sara Malik",   "FR-102"));
        teams.add(new ResponseTeam("Police Unit 1",     "Police Response",   IncidentType.THEFT,    "Insp. Ali Khan",     "PL-201"));
        teams.add(new ResponseTeam("Police Unit 2",     "Police Response",   IncidentType.THEFT,    "Insp. Zara Ahmed",   "PL-202"));
        teams.add(new ResponseTeam("Medical Unit 1",    "Medical & Traffic", IncidentType.ACCIDENT, "Dr. Bilal Sheikh",   "MD-301"));
        teams.add(new ResponseTeam("Medical Unit 2",    "Medical & Traffic", IncidentType.ACCIDENT, "Dr. Hina Farooq",    "MD-302"));
    }
}

// =============================================================================
// REPOSITORIES
// =============================================================================

class AdminRepository {
    public Admin authenticate(String name, String password, String workId) {
        DBHandler db = DBHandler.getInstance();
        synchronized (db) {
            for (String[] a : db.admins)
                if (a[0].equalsIgnoreCase(name.trim()) && a[1].equals(password) && a[2].equalsIgnoreCase(workId.trim()))
                    return new Admin(a[0], a[2]);
        }
        return null;
    }
}

class CitizenRepository {
    private final DBHandler db = DBHandler.getInstance();

    /** Saves a citizen; if the e-mail already exists the stored citizen is returned. */
    public Citizen save(Citizen c) {
        synchronized (db) {
            Citizen existing = db.citizens.get(c.getEmail());
            if (existing != null) return existing;
            db.citizens.put(c.getEmail(), c);
            return c;
        }
    }
    public boolean hasDeclarationAccepted(String email) {
        synchronized (db) { return db.declared.contains(email.trim().toLowerCase()); }
    }
    public void setDeclarationAccepted(String email) {
        synchronized (db) {
            db.declared.add(email.trim().toLowerCase());
            Citizen c = db.citizens.get(email.trim().toLowerCase());
            if (c != null) c.setDeclarationAccepted(true);
        }
    }
    public void addRewardPoints(String email, int pts) {
        synchronized (db) { db.rewards.merge(email.toLowerCase(), pts, Integer::sum); }
    }
    public int getRewardPoints(String email) {
        synchronized (db) { return db.rewards.getOrDefault(email.toLowerCase(), 0); }
    }
    public boolean isBlocked(String email) {
        synchronized (db) { return db.blocked.contains(email.toLowerCase()); }
    }
    public void setBlocked(String email, boolean b) {
        synchronized (db) {
            if (b) db.blocked.add(email.toLowerCase()); else db.blocked.remove(email.toLowerCase());
        }
    }
}

class TeamRepository {
    private final DBHandler db = DBHandler.getInstance();

    public List<ResponseTeam> findAll() {
        synchronized (db) { return new ArrayList<>(db.teams); }
    }
    public ResponseTeam findByName(String name) {
        synchronized (db) {
            for (ResponseTeam t : db.teams) if (t.getTeamName().equals(name)) return t;
        }
        return null;
    }
    public List<ResponseTeam> findAvailableByType(IncidentType type) {
        List<ResponseTeam> out = new ArrayList<>();
        synchronized (db) {
            for (ResponseTeam t : db.teams)
                if (t.getCategory() == type && t.isAvailable()) out.add(t);
        }
        return out;
    }
    public void setAvailability(String teamName, boolean available) {
        ResponseTeam t = findByName(teamName);
        if (t != null) t.setAvailable(available);
    }
}

class IncidentRepository {
    private final DBHandler db = DBHandler.getInstance();

    public String generateId() { return db.nextIncidentId(); }

    public void create(Incident inc, Citizen reporter) {
        inc.addLinkedCitizen(reporter);
        synchronized (db) { db.incidents.put(inc.getIncidentId(), inc); }
    }
    public void linkCitizen(String incidentId, Citizen c) {
        Incident inc = findById(incidentId);
        if (inc != null) inc.addLinkedCitizen(c);
    }
    public void update(Incident inc) {
        synchronized (db) { db.incidents.put(inc.getIncidentId(), inc); }
    }
    public Incident findById(String id) {
        synchronized (db) { return db.incidents.get(id); }
    }
    public Incident findDuplicate(IncidentType type, MapLocation loc) {
        synchronized (db) {
            for (Incident i : db.incidents.values())
                if (i.getType() == type && i.getLocation() == loc && i.getStatus() != IncidentStatus.RESOLVED)
                    return i;
        }
        return null;
    }
    public List<Incident> findAll() {
        synchronized (db) { return new ArrayList<>(db.incidents.values()); }
    }
    public List<Incident> findByLocation(MapLocation loc) {
        List<Incident> out = new ArrayList<>();
        for (Incident i : findAll()) if (i.getLocation() == loc) out.add(i);
        return out;
    }
    public List<Incident> findByCitizen(String email) {
        List<Incident> out = new ArrayList<>();
        for (Incident i : findAll()) if (i.getReporterNumber(email) > 0) out.add(i);
        return out;
    }
    public List<Incident> findByTeamName(String teamName) {
        List<Incident> out = new ArrayList<>();
        for (Incident i : findAll()) if (i.isAssignedTeam(teamName)) out.add(i);
        return out;
    }
    public List<Incident> findOpenDisputes() {
        List<Incident> out = new ArrayList<>();
        for (Incident i : findAll()) if (i.isDisputeRaised() && !i.isDisputeResolved()) out.add(i);
        return out;
    }
}

class DisputeRepository {
    private final DBHandler db = DBHandler.getInstance();

    public void save(Dispute d) {
        synchronized (db) { db.disputes.add(d); }
    }
    public List<Dispute> findOpen() {
        List<Dispute> out = new ArrayList<>();
        synchronized (db) {
            for (Dispute d : db.disputes) if (!d.isResolved()) out.add(d);
        }
        return out;
    }
}