public class Originator {
    private String article;
    public void set(String article){
        this.article = article;
    }

    public Memento storedMemento(){
        return new Memento(article);
    }

    public String restoreMemento(Memento memento){
        //this function is used to get saved article from the memento
        return memento.getSavedArticle();
    }
}
