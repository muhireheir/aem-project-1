package it.codeland.heritier.core.helpers;

public class ArticleImportStatus {
    public int size=0;
    public int skipped=0;
    public int created=0;
    public int count=0;
    public Boolean modified=true;

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSkipped(int skippedPages) {
        this.skipped = skippedPages;
    }

    public void setCreated(int createdPages) {
        this.created = createdPages;
    }

    public void setCount(int x) {
        this.count = x;
    }

    public void setModified() {
        this.modified = false;
    }
    public Boolean isImporting(){
        int sum = this.created + this.skipped;
        if(sum == this.size){
            return false;
        }
        return true;
    }
}
