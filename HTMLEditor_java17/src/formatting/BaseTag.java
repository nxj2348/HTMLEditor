/**
 * 
 */
package formatting;
import java.util.*;

/**
 * @author adam walsh
 * @author Matthew Gallagher
 * @author John Mullen
 * 
 * Represents an HTML tag in the tree
 */
public class BaseTag {
	/**
	 * the tag that this represents
	 */
	protected String tag;//tag value e.g. 'p' for a <p> tag
	
	/**
	 * the link if this tag has one,
	 * can be null
	 */
	protected String link;
	
	/**
	 * is auto indent on?
	 */
	private boolean autoIndent = true;
	
	/**
	 * is this tag collapsed?
	 */
	private boolean collapsed = false;
	
	/**
	 * is this in outline view?
	 */
	private boolean outLineView = false;
	
	/**
	 * the children of this tag
	 */
	private ArrayList<BaseTag> children;
	
	/**
	 * the starting and ending line numbers of this tag
	 */
	protected int lineNumberStart, lineNumberEnd;
	
	/**
	 * constructor
	 */
	public BaseTag() {
		this.children = new ArrayList<BaseTag>();
	}
	
	/**
	 * constructor
	 * @param t - the tag text
	 * @param s - starting line
	 * @param e - ending line
	 * @param c - collapsed?
	 * @param a - children
	 * @param l - the link if this tag has one, can be null
	 */
	public BaseTag(String t, int s, int e, boolean c, ArrayList<BaseTag> a, String l){
		this.tag = t;
		this.collapsed = c;
		this.link = l;
		if(s >= 0)
			lineNumberStart = s;
//System.out.println("start: " + s);
		if(e >= s)
			lineNumberEnd = e;
		else
			lineNumberEnd = lineNumberStart + 1;
//System.out.println("end: " + e);		
		if(a != null)
			children = a;
		else
			this.children = new ArrayList<BaseTag>();
		
	}
	/**
	 * toggles the collapse of this tag
	 */
	public void ToggleCollapse() {
		this.collapsed = !this.collapsed;
	}
	
	/**
	 * gets the content of this tag
	 * @return
	 */
	public String GetContent() {
		return "";//TODO
	}

	
	/**
	 * 
	 * @return gets the current status of whether the system should auto indent
	 */
	public boolean getAutoIndent(){
		return autoIndent;
	}
	
	/**
	 * 
	 * @return switches auto-indentation on and off.
	 */
	public void setAutoIndent(boolean b){
		autoIndent = b;
		if(this.children != null && this.children.size() > 0){
			for(int i = 0; i < this.children.size(); i++){
				this.children.get(i).setAutoIndent(b);
			}
		}
	}
	
	/**
	 * 
	 * @return gets if in outLineView or not
	 */
	public boolean getOutLineView(){
		return outLineView;
	}
	
	/**
	 * 
	 * @return switches OutLineView on and off.
	 */
	public void setOutLineView(boolean b){
		outLineView = b;
		if(this.children != null && this.children.size() > 0){
			for(int i = 0; i < this.children.size(); i++){
				this.children.get(i).setOutLineView(b);
			}
		}
	}
	
	/**
	 *
	 * @param indentLevel
	 * if tag is collapsed
	 * 		@return the tag opener and closer on the same line correctly indented.
	 * if tag is not collapsed
	 * 		@return the tag opener with all its children then the tag closer all on different lines correctly indented.
	 */
	public String getText(int indentLevel){
		String text = "";
		String t = "";
		if(autoIndent){
			//adds the number of indents that was sent
			for(int i = 0; i < indentLevel; i++){
				t += "    ";
			}
		}
		//adds the tag name
		if (link == null) {
			text += t + "<" + tag + ">";
		} else {
			text += t + "<" + tag + " href=\"" + this.link + "\">" ;
		}
		if(!outLineView || !collapsed){
			text += "\n";
			if(this.children != null && this.children.size() > 0){
				//iterates over the children of the tag.
				for(int i = 0; i < this.children.size(); i++){
					//calls this method on all the children
					text += this.children.get(i).getText(indentLevel + 1);
				}
			}else
				text += "\n";
			text += t;
		} else {
			text += "...";
		}
		text += "</" + tag + ">\n";
		return text;
	}
	
	/**
	 *
	 * @param indentLevel
	 * if tag is collapsed
	 * 		@return the tag opener and closer on the same line correctly indented.
	 * if tag is not collapsed
	 * 		@return the tag opener with all its children then the tag closer all on different lines correctly indented.
	 */
	public String getText(int indentLevel, int lineNum){
		String text = "";
		if(lineNum >= 0){
			if(this.inThisTag(lineNum) && this.children != null){
				int c = this.inChildTag(lineNum);
				if( c >= 0){
					text = this.children.get(c).getText(indentLevel + 1, lineNum);
				}else if( c == -1){
					text = getText(indentLevel);
				}
			}
		}
		return text;
	}
	
	/**
	 * gets the indentlevel of this line
	 * @param lineNum - the current line
	 * @return the indent level
	 */
	public int getIndentLevel(int lineNum){
		int indent = 0;
		if(autoIndent == false){
			return indent;
		}
		else if(this.inThisTag(lineNum) && this.children != null){
			int c = this.inChildTag(lineNum);
			if(c >= 0){
				return this.children.get(c).getIndentLevel(lineNum) + 1;
			}
			indent++;
		}
		
		return indent;
	}
	
	/**
	 * gets the children of this tag
	 * @param lineNum - the current line number
	 * @return - the children of this tag
	 */
	public BaseTag getChild(int lineNum){
		//TODO may need to change depending on how text tags are implemented.
System.out.println("GetChild of " + tag + ", sent LineNum = " + lineNum);
		BaseTag child = null;
		if(lineNum >= 0 &&  this.children != null){
			boolean t = this.inThisTag(lineNum);
			if (!t && this.getLineNumberStart() == lineNum)
				t = true;
System.out.println("InThisTag = " + t);
			int c;
			c = this.inChildTag(lineNum);
System.out.println("InChildTag = " + c);

			if( t && c <= -1){
				child = this;
			}else if(c > -1 ){
				child = this.children.get(c).getChild(lineNum);
			}
		}
		return child;
	}
	
	/**
	 * 
	 * @param lineNum
	 * @return child index OR
	 * 		-2 if the number of children is greater than 0 but the line number is not in any of the children. 
	 * 		-1 if there are no children.
	 */
	private int inChildTag(int lineNum){
		if(!(this.children == null) && this.children.size() > 0 ){
			for(int i = 0; i < this.children.size(); i++){
				if(this.children.get(i).inThisTag(lineNum)){
					return i;
				}
			}
			return -2;
		}
		return -1;
	}
	
	protected boolean inThisTag(int lineNum){
		boolean b = false;
		if(this.getLineNumberStart() <= lineNum){ 
			if(this.getLineNumberEnd() >= lineNum){
				b = true;
			}
		}
		return b;
	}
	/**
	 * @param counter
	 * @return the lineNumber of the next line.
	 */
	public int traverseForLineNumbers (int counter){
//System.out.println("traverse For Line Numbers");
		// sets the line number of this tag to be the counter
		this.setLineNumberStart(counter);
//System.out.println("Set <"+ tag + "> lineNumStart to " + counter);
		counter = counter + 1;
		if(this.children != null && this.children.size() > 0){
//System.out.println("number of direct Children is " + this.children.size());
			//iterates over all the children of the tag
			for(int i = 0; i < children.size(); i++){
				//calls this method on each of its children
				counter = children.get(i).traverseForLineNumbers(counter);
			}
		}
		lineNumberEnd = counter;
//System.out.println("Set </" + tag + "> lineNumEnd to " + counter);
		return counter + 1;
	}

	/**
	 * @return the lineNumber
	 */
	public int getLineNumberStart() {
//System.out.println("line number start sent");
		return lineNumberStart;
	}

	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumberStart(int lineNumber) {
//System.out.println("line number end changed");
		this.lineNumberStart = lineNumber;
	}
	
	public boolean addToLineNum(int amount, int lineNum ){
		boolean added = true;
		boolean t = this.inThisTag(lineNum);
//System.out.println("InThisTag = " + t);
		int c = this.inChildTag(lineNum);
//System.out.println("InChildTag = " + c);
		if(t && (c == -1 || c == -2)){
			added = true;
			if(c == -2){
				for (int i = 0; i < children.size(); i++){
					added = this.children.get(i).addToLineNum(amount, lineNum);
//					if(!added)//the amount was not added at some point
//						break;
				}
			}
			if(added){
				this.setLineNumberEnd(this.getLineNumberEnd() + amount);
			}
		}else if(c != -1 && c != -2 && t){
//System.out.println("called addToLineNum on a child");
			added = true;
			//calls this method on the child that the sent line number is in
			added = this.children.get(c).addToLineNum(amount, lineNum);
			//traverses the children after the one at index c
			for(int i = c + 1; i < this.children.size(); i++){
				//calls addToLineNum(amount) on children
				added = this.children.get(i).addToLineNum(amount);
//				if(!added)//the amount was not added at some point
//					break;
			}
			if(added){
//System.out.println("added to the end");
				//adds the amount to the end tag
				this.setLineNumberEnd(this.getLineNumberEnd() + amount);
			}
		}
//		else
//			added = t;
		return added;
	}
	
	/**
	 * 
	 * @param amount
	 * @return
	 */
	protected boolean addToLineNum(int amount){
		boolean added = true;
		//adds sent amount to the start of the tag
		this.setLineNumberStart(this.getLineNumberStart() + amount);
		//checks to see if there are any children
		if (this.children != null && this.children.size() > 0){
			//iterates over all the children
			for( int i = 0; i < this.children.size(); i++){
				//calls this method on all the children
				added = this.children.get(i).addToLineNum(amount);
				if(!added)//the amount was not added at some point
					break;
			}
		}
		//adds the amount to the end of the tag
		this.setLineNumberEnd(this.getLineNumberEnd() + amount);
		return added;
	}
	
	/**
	 * 
	 * @param child
	 * 
	 * Adds the sent child to the end of this baseTag's children
	 */
	public boolean addChild(BaseTag child){
//System.out.println("Added new child to end of list");
		children.add(child);
		return true;
	}
	
	/**
	 * 
	 * @param child
	 * @param lineNum
	 * 
	 * Adds the sent child to the list of children and puts it in the
	 * spot that is specified in the lineNum that was sent.
	 */
	public boolean addChild(BaseTag child, int lineNum){
		boolean added = false;
		// checks to see if the child has been sent to the correct tag
		if(this.inThisTag(lineNum)){
			//check to see if there are any children already
			if(this.children != null && this.children.size() > 0){
				// iterates over all the children backwards
				for(int i = 0; i < this.children.size(); i++){
					// checks to see if the lineNum is greater than the start of the children.get(i)
					if(this.children.get(i).getLineNumberStart() <= lineNum){
						// checks to see if the lineNum is less than the start of the children.get(i)
						if(this.children.get(i).getLineNumberEnd() >= lineNum){
							//calls this function on the child
							added = children.get(i).addChild(child, lineNum);
							break;
						}
					}
				}
				if(!added){
					// iterates over all the children
					for(int i = 0; i < this.children.size(); i++){
						// if the sent line number is greater than or equal to the child's start
						// then the sent child is added to the list of children where that tag is
						// this also moves everything else in the list down one index.
						if(this.children.get(i).getLineNumberStart() >= lineNum){
							this.children.add(i, child);
							added = true;
							break;
						}
					}
				}

			}
			if(!added){
				this.addChild(child);
				added = true;
			}
		}
		return added;
	}

	/**
	 * @return the lineNumberEnd
	 */
	public int getLineNumberEnd() {
		return lineNumberEnd;
	}

	/**
	 * @param lineNumberEnd the lineNumberEnd to set
	 */
	public void setLineNumberEnd(int lineNumberEnd) {
		this.lineNumberEnd = lineNumberEnd;
	}

	/**
	 * Constructs a tag from text
	 * @param text - the text of the tab
	 * @return a new tag
	 */
	public BaseTag tagFromText(String text){
		BaseTag newTag = new BaseTag();
		String[] parts = text.split("(?=<)"); //splits current text into sections per tag
		for (int i = 0; i < parts.length; i++){
			
		}
		return newTag;
	}
	
	/**
	 * this tag to a string
	 */
	public String toString(){
		String text = "";
		//TODO may need to change depending on how text tags are implemented.
		//adds the tag name
		text = text + this.getLineNumberStart() + "    <" + tag + ">";
		if(!outLineView || !collapsed){
			text = text + "\n";
			if(this.children != null && this.children.size() > 0){
				//iterates over the children of the tag.
				for(int i = 0; i < this.children.size(); i++){
				//calls this method on all the children
					text = text + this.children.get(i).toString();
				}	
			}
		}else
			text = text + "...";
		text = text + this.getLineNumberEnd() + "    </" + tag + ">\n";
		return text;
	}
	
	public boolean remove(BaseTag tag){
		if (this.children != null){
			for(int i = 0; i < this.children.size(); i++){
				if (tag == this.children.get(i))
					return true;
			}
			for(int i = 0; i < this.children.size(); i++){
				boolean b = this.children.get(i).remove(tag);
				if(b)
					break;
				
			}
		}
		return false;
	}
}
