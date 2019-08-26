/**
 *
 * @author moham
 */

public class BSTSet {
    private TNode root;
    private int size;
    
    public BSTSet(){
        root = null;
    }
    
    public BSTSet(int[] input){
        int arrayLength = input.length; //this will store the length of the input array.
        int[] filteredInput; //this array will store the sorted input array with no repititions.
        
        //first we are going to sort the array and remove any repetitions.
        int temp; //this will hold the number in the array we are currently at
        
        for(int j=0; j<arrayLength-1; j++){
            for(int i=0; i<arrayLength-1; i++){
                if(input[i]>input[i+1]){
                    temp = input[i];
                    input[i] = input[i+1];
                    input[i+1] = temp;
                }
            }
        }
        
        //the array is now sorted but might have repititions
        for(int i=0; i<arrayLength-1; i++){
            if(input[i] == input[i+1]){
                for(int j=i+1; j<arrayLength-1; j++){
                    input[j] = input[j+1];
                }
                arrayLength-=1; //since we found a repitition, the effective length is shorter by 1
            }
        }
        
        //now we need to move the input array that might be longer to the array that will be the final set
        filteredInput = new int[arrayLength]; //initalizing the array
        
        for(int i=0; i<arrayLength; i++){
            filteredInput[i] = input[i];
        }
        
        //Now we'll make the BST
        root = new TNode(filteredInput[arrayLength/2], null, null);
        TNode tempNode = root;
        
        for(int i=(arrayLength/2)-1; i<arrayLength; i++){
            this.add(filteredInput[i]);
        }
        
        for(int i=(arrayLength/2)-1; i>=0; i--){
            this.add(filteredInput[i]);
        }
        
        this.sTraverse(root);
    }
    
    
    public boolean isIn(int v){
        TNode temp = root;
        
        while(temp != null){
            if(temp.element == v){
                return true;
            }
            else if(temp.element < v){
                temp = temp.right;
            }
            else if(temp.element > v){
                temp = temp.left;
            }
        }
        return false;
    }
    
    public void add(int v){
        TNode temp = root;
        TNode prevTemp = root;
        
        if(root == null){
            root = new TNode(v, null, null);
        }
        
        while(temp != null){
            if(temp.element == v){
                return;
            }
            else if(temp.element < v){
                prevTemp = temp;
                temp = temp.right;
            }
            else if(temp.element > v){
                prevTemp = temp;
                temp = temp.left;
            }
        }
        
        if(prevTemp.element < v){
            temp = prevTemp;
            prevTemp.right = new TNode(v,null,null);
        }
        else if(prevTemp.element > v){
            temp = prevTemp;
            prevTemp.left = new TNode(v,null,null);
        }
    }
    
    public boolean remove(int v){
        TNode temp = root;
        TNode prevTemp = root;
        TNode minBranchNode;
        boolean left = false; //will store whether temp is on the left of prevTemp or not.
        
        while(temp != null){
            if(temp.element == v){
                break;
            }
            else if(temp.element < v){
                prevTemp = temp;
                temp = temp.right;
                left = false;
            }
            else if(temp.element > v){
                prevTemp = temp;
                temp = temp.left;
                left = true;
            }
        }
        
        //couldn't find it
        if(temp == null){
            return false;
        }
        
        //found the node with no children
        else if((temp.left==null) && (temp.right==null)){
            prevTemp = null;
            return true;
        }
        
        //found the node with one child
        else if((temp.left==null) ^ (temp.right==null)){
            if((temp.left==null) && (left == false)){
                prevTemp.right = temp.right;
                return true;
            }
            else if((temp.right==null) && (left == false)){
                prevTemp.right = temp.left;
                return true;
            }
            else if((temp.left==null) && (left == true)){
                prevTemp.left = temp.right;
                return true;
            }
            else if((temp.right==null) && (left == true)){
                prevTemp.left = temp.left;
                return true;
            }
        }
        
        //found the node with 2 children
        else if((temp.left != null) && (temp.right != null)){
            prevTemp = temp;
            temp = temp.right;
            minBranchNode = findMin(temp);
            prevTemp.element = minBranchNode.element;
            minBranchNode = null;
            return true;
        }
        
        //if none of the above cases apply, then we couldn't remove the node.
        return false;
    }
    
    public BSTSet union(BSTSet s){
        BSTSet unionSet = new BSTSet();
        
        unionSet.tAdd(this.root);
        unionSet.tAdd(s.root);
        
        return unionSet;
    }
    
    public BSTSet intersection(BSTSet s){
        BSTSet intersectionSet = new BSTSet();
        
        intersectionSet.tCondAdd(this.root, this, s);
        //intersectionSet.tCondAdd(s.root, this, s); //This line is not needed.
        
        return intersectionSet;
    }
    
    public BSTSet difference(BSTSet s){
        BSTSet differenceSet = new BSTSet();
        
        differenceSet.tCondSub(this.root, this, s);
        
        return differenceSet;
    }
    
    public int size(){
        return size;
    }
    
    public int height(){
        int heightInt;
        int heightMax = 0;
        int heightMin = 0;
        TNode temp;
        
        if(this.root == null){
            return -1;
        }
        
        temp = this.root;
        
        while(temp != null){
            temp = temp.right;
            heightMax++;
        }
        
        while(temp != null){
            temp = temp.left;
            heightMin++;
        }
        
        heightInt = (heightMax >= heightMin) ? heightMax : heightMin;
        
        return heightInt;
    }
    
    public void printBSTSet(){
        if(root==null)
            System.out.println("The set is empty");
        
        else{
            System.out.print("The set elements are: ");
            printBSTSet(root);
            System.out.print("\n");
        }
    }
    
    public void printBSTSet(TNode t){
        if(t!=null){
            printBSTSet(t.left);
            System.out.print(" " + t.element + ", ");
            printBSTSet(t.right);
        }
    }
    
    
    private TNode findMin(TNode n){
        TNode temp = n;
        
        if(temp == null){
            return n;
        }
        
        while(temp.left != null){
            temp = temp.left;
        }
        
        return temp;
    }
    
    private void tAdd(TNode n){
        if(n.left != null){
            tAdd(n.left);
        }
        
        this.add(n.element);
        
        if(n.right != null){
            tAdd(n.right);
        }
    }
    
    private void sTraverse(TNode n){
        if(n.left != null){
            sTraverse(n.left);
        }
        
        this.size++;
        
        if(n.right != null){
            sTraverse(n.right);
        }
    }
    
    private void tCondAdd(TNode n, BSTSet set1, BSTSet set2){
        if(n.left != null){
            tCondAdd(n.left, set1, set2);
        }
        
        if((set1.isIn(n.element)) && (set2.isIn(n.element))){
            this.add(n.element);
        }
        
        if(n.right != null){
            tCondAdd(n.right, set1, set2);
        }
    }
    
    private void tCondSub(TNode n, BSTSet set1, BSTSet set2){
        if(n.left != null){
            tCondSub(n.left, set1, set2);
        }
        
        if((set1.isIn(n.element) == true) && (set2.isIn(n.element) == false)){
            this.add(n.element);
        }
        
        if(n.right != null){
            tCondSub(n.right, set1, set2);
        }
    }
}
