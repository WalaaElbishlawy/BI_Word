package invertedIndex;

public class Posting {

    public Posting next = null;
    int docId;
    int position; // Add position field
    int dtf; // Add dtf field

    Posting(int id, int pos, int dtf) { // Modify constructor to accept position and dtf
        docId = id;
        position = pos; // Initialize position
        this.dtf = dtf; // Initialize dtf
    }

    Posting(int id, int dtf) { // Modify constructor to accept dtf
        docId = id;
        position = -1; // Default position value
        this.dtf = dtf; // Initialize dtf
    }

    Posting(int id) { // Modify constructor to accept position and dtf
        docId = id;
        position = -1; // Default position value
        dtf = 1; // Default dtf value
    }
}
