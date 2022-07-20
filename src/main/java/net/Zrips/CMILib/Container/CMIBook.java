package net.Zrips.CMILib.Container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.Zrips.CMILib.Colors.CMIChatColor;

public class CMIBook {

    ItemStack item;

    HashMap<Integer, List<String>> content = new HashMap<Integer, List<String>>();

    public CMIBook() {
	this.item = new ItemStack(Material.WRITTEN_BOOK);
    }

    public ItemStack getItemStack() {

	if (!hasAuthor())
	    this.setAuthor("None");
	if (!hasTitle())
	    this.setTitle("None");

	BookMeta bm = (BookMeta) item.getItemMeta();

	List<String> pages = new ArrayList<String>();

	for (Entry<Integer, List<String>> oneP : content.entrySet()) {
	    String page = "";
	    for (String one : oneP.getValue()) {
		page += CMIChatColor.translate(one) + " \n";
	    }
//	    pages.add(page);
	    bm.addPage(page);
	}
	item.setItemMeta(bm);
//	CMI.getInstance().getRef().setPages(bm, pages);
	return item;
    }

    public boolean hasAuthor() {
	BookMeta bm = (BookMeta) item.getItemMeta();
	return bm.hasAuthor();
    }

    public boolean hasTitle() {
	BookMeta bm = (BookMeta) item.getItemMeta();
	return bm.hasTitle();
    }

    public CMIBook setTitle(String text) {
	BookMeta bm = (BookMeta) item.getItemMeta();
	bm.setTitle(text);
	item.setItemMeta(bm);
	return this;
    }

    public CMIBook setAuthor(String text) {
	BookMeta bm = (BookMeta) item.getItemMeta();
	bm.setAuthor(text);
	item.setItemMeta(bm);
	return this;
    }

    public CMIBook addNewPage() {
	int page = content.size() + 1;
	List<String> lines = new ArrayList<String>();
	content.put(page, lines);
	return this;
    }

    public CMIBook addLine(String text) {
	int page = 1;
	List<String> lines = new ArrayList<String>();
	if (!content.isEmpty()) {
	    page = content.size();
	    lines = content.get(content.size());
	}

	if (lines.size() >= 14) {
	    addNewPage();
	    this.addLine(text);
	    return this;
	}

	ArrayList<String> t = new ArrayList<String>(lines);
	t.add(text);

	if (linesToString(t, true).length() > 300) {
	    addNewPage();
	    this.addLine(text);
	    return this;
	}

	lines.add(text);
	content.put(page, lines);
	return this;
    }

    private static String linesToString(List<String> lines, boolean clean) {
	String page = "";
	for (String one : lines) {
	    if (clean)
		page += one;
	    else
		page += one + " \n";
	}
	return page;
    }

//    public void showFake(CommandSender sender) {
//	if (sender instanceof Player)
//	    showFake((Player) sender);
//    }
//
//    public void showFake(Player player) {
//	if (!hasAuthor())
//	    this.setAuthor("None");
//	if (!hasTitle())
//	    this.setTitle("None");
//
//	BookMeta bm = (BookMeta) item.getItemMeta();
//
//	List<String> pages = new ArrayList<String>();
//	
//	for (Entry<Integer, List<String>> oneP : content.entrySet()) {
//	    String page = "";
//	    for (String one : oneP.getValue()) {
//		page += CMIChatColor.translate( one) + " \n";
//	    }
//	    pages.add(page);
////	    bm.addPage(page);
//	}
////	item.setItemMeta(bm);
//	
//	CMI.getInstance().getRef().setPages(bm, pages);
//
//	CMI.getInstance().getRef().openBook(item, player);
//
//    }

}
