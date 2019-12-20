/***************************************************************************
 *
 * A Linked List class with a private static inner Node class.
 * by Dr. Victor Adamchik
 *
 *****************************************************************************/
 
import java.util.*;

public class LinkedList<AnyType> implements Iterable<AnyType>
{
   private Node<AnyType> head;

 /**
   *  Constructs an empty list
   */
   public LinkedList()
   {
      head = null;
   }
 /**
   *  Returns true if the list is empty
   *
   */
   public boolean isEmpty()
   {
      return head == null;
   }
 /**
   *  Inserts a new node at the beginning of this list.
   *
   */
   public void addFirst(AnyType item)
   {
      head = new Node<AnyType>(item, head);
   }
 /**
   *  Returns the first element in the list.
   *
   */
   public AnyType getFirst()
   {
      if(head == null) throw new NoSuchElementException();

      return head.data;
   }
 /**
   *  Removes the first element in the list.
   *
   */
   public AnyType removeFirst()
   {
      AnyType tmp = getFirst();
      head = head.next;
      return tmp;
   }
 /**
   *  Inserts a new node to the end of this list.
   *
   */
   public void addLast(AnyType item)
   {
      if( head == null)
         addFirst(item);
      else
      {
         Node<AnyType> tmp = head;
         while(tmp.next != null) tmp = tmp.next;

         tmp.next = new Node<AnyType>(item, null);
      }
   }
 /**
   *  Returns the last element in the list.
   *
   */
   public AnyType getLast()
   {
      if(head == null) throw new NoSuchElementException();

      Node<AnyType> tmp = head;
      while(tmp.next != null) tmp = tmp.next;

      return tmp.data;
   }
 /**
   *  Removes all nodes from the list.
   *
   */
   public void clear()
   {
      head = null;
   }
 /**
   *  Returns true if this list contains the specified element.
   *
   */
   public boolean contains(AnyType x)
   {
      for(AnyType tmp : this)
         if(tmp.equals(x)) return true;

      return false;
   }
 /**
   *  Returns the data at the specified position in the list.
   *
   */
   public AnyType get(int pos)
   {
      if (head == null) throw new IndexOutOfBoundsException();

      Node<AnyType> tmp = head;
      for (int k = 0; k < pos; k++) tmp = tmp.next;

      if( tmp == null) throw new IndexOutOfBoundsException();

      return tmp.data;
   }
 /**
   *  Returns a string representation
   *
   */
   public String toString()
   {
      StringBuffer result = new StringBuffer();
      for(Object x : this)
      	result.append(x + " ");

      return result.toString();
   }
 /**
   *  Inserts a new node after a node containing the key.
   *
   */
   public void insertAfter(AnyType key, AnyType toInsert)
   {
      Node<AnyType> tmp = head;

      while(tmp != null && !tmp.data.equals(key)) tmp = tmp.next;

      if(tmp != null)
         tmp.next = new Node<AnyType>(toInsert, tmp.next);
   }
 /**
   *  Inserts a new node before a node containing the key.
   *
   */
   public void insertBefore(AnyType key, AnyType toInsert)
   {
      if(head == null) return;

      if(head.data.equals(key))
      {
         addFirst(toInsert);
         return;
      }

      Node<AnyType> prev = null;
      Node<AnyType> cur = head;

      while(cur != null && !cur.data.equals(key))
      {
         prev = cur;
         cur = cur.next;
      }
      //insert between cur and prev
      if(cur != null)
         prev.next = new Node<AnyType>(toInsert, cur);
   }
 /**
   *  Removes the first occurrence of the specified element in this list.
   *
   */
   public void remove(AnyType key)
   {
      if(head == null)
         throw new RuntimeException("cannot delete");

      if( head.data.equals(key) )
      {
         head = head.next;
         return;
      }

      Node<AnyType> cur  = head;
      Node<AnyType> prev = null;

      while(cur != null && !cur.data.equals(key) )
      {
         prev = cur;
         cur = cur.next;
      }

      if(cur == null)
         throw new RuntimeException("cannot delete");

      //delete cur node
      prev.next = cur.next;
   }
 
 /**
   *  Returns a deep copy of the list
   *  Complexity: O(n)
   */
   public LinkedList<AnyType> copy()
   {
      LinkedList<AnyType> twin = new LinkedList<AnyType>();
      Node<AnyType> tmp = head;
      while(tmp != null)
      {
         twin.addFirst( tmp.data );
         tmp = tmp.next;
      }

      return twin.reverse();
   }
 /**
   *  Reverses the list
   *  Complewxity: O(n)
   */
   public LinkedList<AnyType> reverse()
   {
      LinkedList<AnyType> list = new LinkedList<AnyType>();
      Node<AnyType> tmp = head;
      while(tmp != null)
      {
         list.addFirst( tmp.data );
         tmp = tmp.next;
      }
      return list;
   }
   
   
   /**
    *  Inserts a new node before a node containing the key.
    *
    */
    public void insertSort(AnyType key, Comparator<AnyType> cmp)
    {
       if(head == null) { 
    	   head = new Node<AnyType>(key, null);       
    	   return;
       }
       
       if(cmp.compare(key, head.data) <=0){
    	   addFirst(key);
    	   return;
       }
       
       Node<AnyType> prev = null;
       Node<AnyType> cur = head;

       while(cur != null && (cmp.compare(key,  cur.data)>0))
       {
          prev = cur;
          cur = cur.next;
       }
       //insert between prev and cur
       prev.next = new Node<AnyType>(key, cur);  
    }
    
   /**
     * Merge Sort
     * 
     */
    
    Node<AnyType> getHead(){
    	return head;
    }
    
    void setHead(Node<AnyType> n) {
    	head = n;
    }
    
    Node<AnyType> mergeSort(Node<AnyType> h) {
		
    	
    	if (h == null || h.next == null) {
    		return h;
    	}
    	
    	Node<AnyType> middle = getMiddle(h);
    	Node<AnyType> middle_next = middle.next;
    	
    	middle.next = null;
    	
    	Node<AnyType> left = mergeSort(h);
    	Node<AnyType> right = mergeSort(middle_next);
    	
    	Node<AnyType> sorted_list = sortedMerge(left, right);
    	
    	return sorted_list;
    }
    
    Node<AnyType> getMiddle(Node<AnyType> h){
    	
    	if (h == null) {
    		return h;
    	}
    	
    	Node<AnyType> slow = h;
    	Node<AnyType> fast = h;
    	
    	while (fast.next != null && fast.next.next != null) {
    		slow = slow.next;
    		fast = fast.next.next;
    	}
    	
    	return slow;
    	
    }
    
    Node<AnyType> sortedMerge(Node<AnyType> a, Node<AnyType> b){
    	
    	Node<AnyType> result = null;
    	
    	if (a == null) {
    		return b;
    	}
    	if (b == null) {
    		return a;
    	}
    	
    	int cmp;
    	
    	if (a.data instanceof KV && b.data instanceof KV) {
    		cmp = ((KV)a.data).compareTo((KV)b.data);
    	} else {
    		cmp = a.data.toString().compareTo(b.data.toString());
    	}
    	
    	if (cmp <= 0) {
    		result = a;
    		result.next = sortedMerge(a.next, b);
    	} else {
    		result = b;
    		result.next = sortedMerge(a, b.next);
    	}
    	
    	return result;
    }
    
    
   
 /*******************************************************
 *
 *  The Node class
 *
 ********************************************************/
   private static class Node<AnyType>
   {
      private AnyType data;
      private Node<AnyType> next;

      public Node(AnyType data, Node<AnyType> next)
      {
         this.data = data;
         this.next = next;
      }
   }
   public LinkedList<AnyType> handOver() {
	   LinkedList <AnyType> newList = new LinkedList();
	   newList.head = this.head;
	   head = null;
	   return newList;
	}
 /*******************************************************
 *
 *  The Iterator class
 *
 ********************************************************/

   public Iterator<AnyType> iterator()
   {
      return new LinkedListIterator();
   }

   class LinkedListIterator implements Iterator<AnyType>
   {
      private Node<AnyType> nextNode;

      public LinkedListIterator()
      {
         nextNode = head;
      }

      public boolean hasNext()
      {
         return nextNode != null;
      }

      public AnyType next()
      {
         if (!hasNext()) throw new NoSuchElementException();
         AnyType res = nextNode.data;
         nextNode = nextNode.next;
         return res;
      }
      public AnyType peek() {
    	  if (!hasNext()) throw new NoSuchElementException();
          AnyType res = nextNode.data;
          return res;    	  
      }
      public void remove() { throw new UnsupportedOperationException(); }
   }
   



/*****   Include the main() for testing and debugging  *****/


   public static void main(String[] args)
   {
      LinkedList<String> list = new LinkedList <String>();
      
      /*
      list.addFirst("p");
      list.addFirst("a");
      list.addFirst("e");
      list.addFirst("h");
      System.out.println(list);

		LinkedList<String> twin = list.copy();
      System.out.println(twin);

      System.out.println(list.get(0));
//		System.out.println(list.get(4));   //exception

      list.addLast("s");
      Iterator<String> itr = list.iterator();
      while(itr.hasNext())
         System.out.print(itr.next() + " ");
      System.out.println();

      for(Object x : list)
         System.out.print(x + " ");
      System.out.println();

      list.insertAfter("e", "ee");
      System.out.println(list);
      System.out.println(list.getLast());

      list.insertBefore("h", "yy");
      System.out.println(list);

      list.remove("p");
      System.out.println(list);
      */
      
      list.addFirst("a");
      list.addFirst("c");
      list.addFirst("b");
      list.addFirst("h");
      list.addFirst("d");
      list.addFirst("e");
      System.out.println(list);
      
      list.setHead(list.mergeSort(list.getHead()));
      System.out.println(list);
	}


}