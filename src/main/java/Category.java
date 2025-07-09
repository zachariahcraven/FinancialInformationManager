public class Category {

    int allocated;
    int allocatedChange;
    int spent;
    int spentChange;
    int remaining;
    int remainingChange;
    String name;

    public Category() {
        this.spent = 0;
        this.spentChange = 0;
    }

    public void setAllocated(int allocated) {
        this.allocated = allocated;
    }

    public void setAllocatedChange(int allocatedChange) {
        this.allocatedChange = allocatedChange;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public void setRemainingChange(int remainingChange) {
        this.remainingChange = remainingChange;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSpent(int spent) {
        this.spent += spent;
    }

    public void addSpentChange(int spentChange) {
        this.spentChange += spentChange;
    }

    public void updateRemaining(int spent) {
        this.remaining -= spent;
    }

    public void updateRemainingChange(int spentChange) {
        this.remainingChange -= spentChange;
    }

    public String getName() {
        return name;
    }

    public int getAllocated() {
        return allocated;
    }

    public int getAllocatedChange() {
        return allocatedChange;
    }

    public int getSpent() {
        return spent;
    }

    public int getSpentChange() {
        return spentChange;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getRemainingChange() {
        return remainingChange;
    }
}
