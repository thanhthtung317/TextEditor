public class Memento {
    private String article;

    Memento(String article){
        this.article = article;
    }

    public String getSavedArticle(){
        return this.article;
    }
}
