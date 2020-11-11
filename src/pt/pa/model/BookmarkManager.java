package pt.pa.model;

import pt.pa.adts.Position;
import pt.pa.adts.Tree;
import pt.pa.adts.TreeLinked;

public class BookmarkManager {
   private Tree<BookmarkEntry> bookmarks;

   public BookmarkManager(){
       bookmarks = new TreeLinked<>(new BookmarkEntry("Bookmarks"));
   }

    private Position<BookmarkEntry> find(String key){
        for(Position<BookmarkEntry> bookmark: bookmarks.positions()){
            if (bookmark.element().getKey().equalsIgnoreCase(key)){
                return bookmark;
            }
        }
        return null;
    }

    private boolean exists(String key){
       return (find(key) != null);
    }

    public void addBookmarkFolder(String keyParent, String keyFolder) throws BookmarkInvalidOperation{
        if(!exists(keyParent)){
            throw new BookmarkInvalidOperation("Parent key does not exist");
        }

        if(exists(keyFolder)){
            throw new BookmarkInvalidOperation("Folder key already exists");
        }

        bookmarks.insert(find(keyParent),new BookmarkEntry(keyFolder));
    }

    public void addBookmarkEntry(String keyParent, String keyFolder, String url) throws BookmarkInvalidOperation{
        if(!exists(keyParent)){
            throw new BookmarkInvalidOperation("Parent folder " + keyParent + " does not exist");
        }

        if(exists(keyFolder)){
            throw new BookmarkInvalidOperation("Entry" + keyFolder + " already exists");
        }

        Position<BookmarkEntry> parentPosition = find(keyParent);

        bookmarks.insert(parentPosition,new BookmarkEntry(keyFolder,url));
    }

    public int getTotalEntries(){
        return bookmarks.size()-1;
    }

    public String getParentFolder(String keyEntry) throws BookmarkInvalidOperation{
        Position<BookmarkEntry> position = find(keyEntry);
        if(position == null){
            throw new BookmarkInvalidOperation("keyEntry does not exist");
        }
        if(position.element().isFolder()){
            throw new BookmarkInvalidOperation("keyEntry does not exist");
        }
        Position<BookmarkEntry> positionParent = bookmarks.parent(position);
        return positionParent.element().getKey();
    }

    public int getTotalFolders(){
       int count = 0;
        for(Position<BookmarkEntry> bookmark: bookmarks.positions()){
            if (bookmark.element().isFolder()){
                count++;
            }
        }
        return count;
    }

    public String fullPathOf (String keyEntry) throws BookmarkInvalidOperation{
       String result = "";
        Position<BookmarkEntry> position = find(keyEntry);
        if(position == null){
            throw new BookmarkInvalidOperation("keyEntry does not exist");
        }
        result += "Entry: " + position.element().getUrl() + " --> ";
        result += "Folder: " + position.element().getKey() + " --> ";
        while(!bookmarks.isRoot(position)){
            result += "Folder: " + bookmarks.parent(position).element() + " --> ";
            position = bookmarks.parent(position);
        }
       return result;
    }
}
