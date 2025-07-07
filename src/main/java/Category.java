public class Category {

    int allocated;
    int allocatedChange;
    int spent;
    int spentChange;
    int remaining;
    int remainingChange;
    String name;

    public Category() {

    }

    public void setAllocated(int allocated) {
        this.allocated = allocated;
    }

    public void setAllocatedChange(int allocatedChange) {
        this.allocatedChange = allocatedChange;
    }

    public void setSpent(int spent) {
        this.spent = spent;
    }

    public void setSpentChange(int spentChange) {
        this.spentChange = spentChange;
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

    public String getName() {
        return name;
    }
}
