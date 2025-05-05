package map;

import java.util.*;
public final class Node {
    public final int id;             // useful for debugging / UI prints
    public Node next;                // always non�몁ull except for HOME
    public Node warp;                // null unless this is an entry square
    Node(int id) {
        this.id = id;
    }
}

public class Board {
    static int start_index =0;
    // ---------- fields ----------------------------------------------------
    private final Node start;                    // entry square
    private final Node end;                     // final square

    // ---------- ctor: build the standard 29�몊quare board ------------------
    public Board(int style) {
        // Build nodes
        if (style == 4) {
            Node[] n = new Node[29];          // 0=start, 27=home, 28=centre
            for (int i = 0; i < n.length; i++) n[i] = new Node(i);


            // Regular outer track (counter�멵lockwise)
            for (int i = 0; i < 27; i++) n[i].next = n[(i + 1) % 28];
            n[27] = n[0];                     // �쐆ome�� re�멷nters start (ends turn)

            // Diagonals (warp edges) entering centre (#28)
            n[5].warp = n[20];
            n[10].warp = n[22];
            n[21].next = n[28];
            n[23].next = n[28];
            n[19].next = n[0];
            // Centre diagonal exits
            n[28].next = n[26];
            n[25].next = n[15];

            start = n[0];
            end = n[27];
        }
        else if (style == 5) {
            Node[] n = new Node[36];
            for (int i = 0; i < n.length; i++) n[i] = new Node(i);

            // Regular outer track (counter�멵lockwise)
            for (int i = 0; i < 34; i++) n[i].next = n[(i + 1) % 35];
            n[34] = n[0];                     // �쐆ome�� re�멷nters start (ends turn)


            n[5].warp = n[35];
            n[10].warp = n[35];
            n[15].warp = n[35];
            // Centre diagonal exits
            n[35].next = n[20];               // centre �넂 20 �넂 21 ��

            start = n[0];
            end = n[34];
        }
        else if (style == 6) {
            Node[] n = new Node[43];
            for (int i = 0; i < n.length; i++) n[i] = new Node(i);

            // Regular outer track (counter�멵lockwise)
            for (int i = 0; i < 41; i++) n[i].next = n[(i + 1) % 42];
            n[41] = n[0];                     // �쐆ome�� re�멷nters start (ends turn)

            n[5].warp = n[30];
            n[10].warp = n[];
            n[15].warp = n[42];
            n[20].warp = n[42];
            // Centre diagonal exits
            n[42].next = n[20];               // centre �넂 20 �넂 21 ��

            start = n[0];
            end = n[41];
        }
    }
    private static int followPath(int startindex, int steps, int style) { if(style == 4){

        if(startindex!=5 && startindex!=10 && startindex!=15){
            return startindex + steps;
        }
        if((startindex == 26 || startindex == 16) && steps == 5){
            return -2;
        }
        if((startindex == 17 || startindex == 22) && steps == 4){
            return -2;
        }
        if((startindex == 21 || startindex == 18) && steps == 3){
            return -2;
        }
        if((startindex == 20 || startindex == 19) && steps == 2){
            return -2;
        }
        if((startindex == 25 || startindex == 15) && steps == 5) {
            return start_index;
        }
        if((startindex == 26 || startindex == 16) && steps == 4) {
            return start_index;
        }
        if((startindex == 17 || startindex == 22) && steps == 3){
            return start_index;
        }
        if((startindex == 21 || startindex == 18) && steps == 2) {
            return start_index;
        }
        if((startindex == 20 || startindex == 19) && steps == 1){
            return start_index;
        }
        if(startindex == 25){
            if(steps==2){return 22;}
            else if(steps == 3){return 21;}
            else if(steps == 4){return 20;}
        }
        if(startindex == 23){
            if(steps==2){return 22;}
            else if(steps == 3){return 28;}
            else if(steps == 4){return 27;}
            else if(steps == 5){return 15;}
        }
        if(startindex == 24){
            if(steps==1){return 22;}
            else if(steps == 2){return 28;}
            else if(steps == 3){return 27;}
            else if(steps == 4){return 15;}
            else if(steps == 5){return 16;}
        }
        if(startindex == 26){
            if(steps==1){return 22;}
            else if(steps == 2){return 21;}
            else if(steps == 3){return 20;}
        }
        if(startindex == 21){
            if(steps==1){return 20;}
        }
        if(startindex == 28){
            if(steps==1){return 27;}
            else if(steps == 2){return 15;}
            else if(steps == 3){return 16;}
            else if(steps == 4){return 17;}
            else if(steps == 5){return 18;}
        }
        if(startindex == 27){
                if(steps==1){return 15;}
                else if(steps == 2){return 16;}
                else if(steps == 3){return 17;}
                else if(steps == 4){return 18;}
                else if(steps == 5){return 19;}
            }
            if(startindex == 22){
                if(steps==1){return 21;}
                else if(steps==2){return 20;}
            }
            if(startindex==5){
                if(steps == 1){
                    return 23;}
                if(steps == 2) {
                    return 24;
                }
                if(steps == 3){
                    return 22;
                }
                if(steps == 4){
                    return 21;
                }
                if(steps == 5){
                    return 20;
                }
                if(startindex==10){
                    if(steps == 1){
                        return 25;
                    }
                    if(steps == 2){
                        return 26;
                    }
                    if(steps == 3){
                        return 22;
                    }
                    if(steps == 4){
                        return 21;
                    }
                    if(steps == 5){
                        return 20;
                    }
                }
            }
    
            else if(style == 5){
                if(startindex!=5 && startindex!=10 && startindex!=15 && startindex!=20){
                    return startindex + steps;
                }
                if((startindex == 21) && steps == 5){
                    return -2;
                }
                if((startindex == 23 || startindex == 26 ) && steps == 3){
                    return -2;
                }
                if((startindex == 27 || startindex == 22) && steps == 4) {
                    return -2;
                }
                if((startindex == 25 || startindex == 24) && steps == 2) {
                    return -2;
                }
                if(startindex==25 || startindex == 24){
                    if(steps == 1){
                        return start_index;
                    }
                    if(startindex==26 || startindex == 23){
                        if(steps == 2){
                            return start_index;
                        }
                        if(startindex==22 || startindex == 27){
                            if(steps == 3){
                                return start_index;
                            }
                            if(startindex==21){
                                if(steps == 4){
                                    return start_index;
                                }
                                if(startindex==20){
                                    if(steps == 5){
                                        return start_index;
                                    }
                                    if(startindex==28){
                                        if(steps == 1){
                                            return 29;
                                        }
                                        if(steps == 2){
                                            return 27;
                                        }
                                        if(steps == 3){
                                            return 35;
                                        }
                                        if(steps == 4){
                                            return 34;
                                        }
                                        if(steps == 5){
                                            return 20;
                                        }
                                        if(startindex==29){
                                            if(steps == 1){
                                                return 27;
                                            }
                                            if(steps == 2){
                                                return 35;
                                            }
                                            if(steps == 3){
                                                return 34;
                                            }
                                            if(steps == 4){
                                                return 20;
                                            }
                                            if(steps == 5){
                                                return 21;
                                            }
                                            if(startindex==30){
                                                if(steps == 1){
                                                    return 31;
                                                }
                                                if(steps == 2){
                                                    return 27;
                                                }
                                                if(steps == 3){
                                                    return 35;
                                                }
                                                if(steps == 4){
                                                    return 34;
                                                }
                                                if(steps == 5){
                                                    return 20;
                                                }
                                                if(startindex==31){
                                                    if(steps == 1){
                                                        return 27;
                                                    }
                                                    if(steps == 2){
                                                        return 35;
                                                    }
                                                    if(steps == 3){
                                                        return 34;
                                                    }
                                                    if(steps == 4){
                                                        return 20;
                                                    }
                                                    if(steps == 5){
                                                        return 21;
                                                    }
                                                    if(startindex==32){
                                                        if(steps == 1){
                                                            return 33;
                                                        }
                                                        if(steps == 2){
                                                            return 27;
                                                        }
                                                        if(steps == 3){
                                                            return 35;
                                                        }
                                                        if(steps == 4){
                                                            return 34;
                                                        }
                                                        if(steps == 5){
                                                            return 20;
                                                        }
                                                        if(startindex==33){
                                                            if(steps == 1){
                                                                return 27;
                                                            }
                                                            if(steps == 2){
                                                                return 35;
                                                            }
                                                            if(steps == 3){
                                                                return 34;
                                                            }
                                                            if(steps == 4){
                                                                return 20;
                                                            }
                                                            if(steps == 5){
                                                                return 21;
                                                            }
                                                            if(startindex==34){
                                                                if(steps == 1){
                                                                    return 20;
                                                                }
                                                                if(steps == 2){
                                                                    return 21;
                                                                }
                                                                if(steps == 3){
                                                                    return 22;
                                                                }
                                                                if(steps == 4){
                                                                    return 23;
                                                                }
                                                                if(steps == 5){
                                                                    return 24;
                                                                }
                                                                if(startindex==35){
                                                                    if(steps == 1){
                                                                        return 34;
                                                                    }
                                                                    if(steps == 2){
                                                                        return 20;
                                                                    }
                                                                    if(steps == 3){
                                                                        return 21;
                                                                    }
                                                                    if(steps == 4){
                                                                        return 22;
                                                                    }
                                                                    if(steps == 5){
                                                                        return 23;
                                                                    }
                                                                    if(startindex==26){
                                                                        if(steps == 1){
                                                                            return 25;
                                                                        }
                                                                        if(startindex==27){
                                                                            if(steps == 1){
                                                                                return 26;
                                                                            }
                                                                            if(steps == 2){
                                                                                return 25;
                                                                            }
                                                                            if(startindex==5){
                                                                                if(steps == 1){
                                                                                    return 28;
                                                                                }
                                                                                if(steps == 2){
                                                                                    return 29;
                                                                                }
                                                                                if(steps == 3){
                                                                                    return 27;
                                                                                }
                                                                                if(steps == 4){
                                                                                    return 35;
                                                                                }
                                                                                if(steps == 5){
                                                                                    return 34;
                                                                                }
                                                                            }
                                                                            if(startindex == 10){
                                                                                if(steps == 1){
                                                                                    return 30;
                                                                                }
                                                                                if(steps == 2){
                                                                                    return 31;
                                                                                }
    
                                                                                if(steps == 3){
                                                                                    return 27;
                                                                                }
                                                                                if(steps == 4){
                                                                                    return 35;
                                                                                }
                                                                                if(steps == 5){
                                                                                    return 34;
                                                                                }
    
                                                                            }
                                                                        }
                                                                        else if(style == 6){
                                                                                    if(startindex!=5 && startindex!=10 && startindex!=15 && startindex!=20){
                                                                                        return startindex + steps;
                                                                                    }
                                                                                    if((startindex == 26) && steps == 5){
                                                                                        return -2;
                                                                                    }
                                                                                    if((startindex == 28 || startindex == 31 ) && steps == 3){
                                                                                        return -2;
                                                                                    }
                                                                                    if((startindex == 27 || startindex == 32) && steps == 4) {
                                                                                        return -2;
                                                                                    }
                                                                                    if((startindex == 29 || startindex == 30) && steps == 2) {
                                                                                        return -2;
                                                                                    }
                                                                                    if((startindex == 29 || startindex == 30) && steps == 1) {
                                                                                        return start_index;
                                                                                    }
                                                                                    if((startindex == 28 || startindex == 31) && steps == 2) {
                                                                                        return start_index;
                                                                                    }
                                                                                    if((startindex == 27 || startindex == 32) && steps == 3) {
                                                                                        return start_index;
                                                                                    }
                                                                                    if((startindex == 26) && steps == 4) {
                                                                                        return start_index;
                                                                                    }
                                                                                    if((startindex == 25) && steps == 5) {
                                                                                        return start_index;
                                                                                    }
                                                                                    if(startindex==33){
                                                                                        if(steps == 1){
                                                                                            return 34;
                                                                                        }
                                                                                        if(steps == 2){
                                                                                            return 32;
                                                                                        }
                                                                                        if(steps == 3){
                                                                                            return 42;
                                                                                        }
                                                                                        if(steps == 4){
                                                                                            return 41;
                                                                                        }
                                                                                        if(steps == 5){
                                                                                            return 25;
                                                                                        }
                                                                                        if(startindex==34){
                                                                                            if(steps == 1){
                                                                                                return 32;
                                                                                            }
                                                                                            if(steps == 2){
                                                                                                return 42;
                                                                                            }
                                                                                            if(steps == 3){
                                                                                                return 41;
                                                                                            }
                                                                                            if(steps == 4){
                                                                                                return 25;
                                                                                            }
                                                                                            if(steps == 5){
                                                                                                return 26;
                                                                                            }
                                                                                            if(startindex==35){
                                                                                                if(steps == 1){
                                                                                                    return 36;
                                                                                                }
                                                                                                if(steps == 2){
                                                                                                    return 32;
                                                                                                }
                                                                                                if(steps == 3){
                                                                                                    return 42;
                                                                                                }
                                                                                                if(steps == 4){
                                                                                                    return 41;
                                                                                                }
                                                                                                if(steps == 5){
                                                                                                    return 25;
                                                                                                }
                                                                                                if(startindex==36){
                                                                                                    if(steps == 1){
                                                                                                        return 32;
                                                                                                    }
                                                                                                    if(steps == 2){
                                                                                                        return 42;
                                                                                                    }
                                                                                                    if(steps == 3){
                                                                                                        return 41;
                                                                                                    }
                                                                                                    if(steps == 4){
                                                                                                        return 25;
                                                                                                    }
                                                                                                    if(steps == 5){
                                                                                                        return 26;
                                                                                                    }
                                                                                                    if(startindex==37){
                                                                                                        if(steps == 1){
                                                                                                            return 38;
                                                                                                        }
                                                                                                        if(steps == 2){
                                                                                                            return 32;
                                                                                                        }
                                                                                                        if(steps == 3){
                                                                                                            return 42;
                                                                                                        }
                                                                                                        if(steps == 4){
                                                                                                            return 41;
                                                                                                        }
                                                                                                        if(steps == 5){
                                                                                                            return 25;
                                                                                                        }
                                                                                                        if(startindex==38){
                                                                                                            if(steps == 1){
                                                                                                                return 32;
                                                                                                            }
                                                                                                            if(steps == 2){
                                                                                                                return 42;
                                                                                                            }
                                                                                                            if(steps == 3){
                                                                                                                return 41;
                                                                                                            }
                                                                                                            if(steps == 4){
                                                                                                                return 25;
                                                                                                            }
                                                                                                            if(steps == 5){
                                                                                                                return 26;
                                                                                                            }
                                                                                                            if(startindex==39){
                                                                                                                if(steps == 1){
                                                                                                                    return 40;
                                                                                                                }
                                                                                                                if(steps == 2){
                                                                                                                    return 32;
                                                                                                                }
                                                                                                                if(steps == 3){
                                                                                                                    return 42;
                                                                                                                }
                                                                                                                if(steps == 4){
                                                                                                                    return 41;
                                                                                                                }
                                                                                                                if(steps == 5){
                                                                                                                    return 25;
                                                                                                                }
                                                                                                                if(startindex==40){
                                                                                                                    if(steps == 1){
                                                                                                                        return 32;
                                                                                                                    }
                                                                                                                    if(steps == 2){
                                                                                                                        return 42;
                                                                                                                    }
                                                                                                                    if(steps == 3){
                                                                                                                        return 41;
                                                                                                                    }
                                                                                                                    if(steps == 4){
                                                                                                                        return 25;
                                                                                                                    }
                                                                                                                    if(steps == 5){
                                                                                                                        return 26;
                                                                                                                    }
                                                                                                                    if(startindex==41){
                                                                                                                        if(steps == 1){
                                                                                                                            return 25;
                                                                                                                        }
                                                                                                                        if(steps == 2){
                                                                                                                            return 26;
                                                                                                                        }
                                                                                                                        if(steps == 3){
                                                                                                                            return 27;
                                                                                                                        }
                                                                                                                        if(steps == 4){
                                                                                                                            return 28;
                                                                                                                        }
                                                                                                                        if(steps == 5){
                                                                                                                            return 29;
                                                                                                                        }
                                                                                                                        if(startindex==42){
                                                                                                                            if(steps == 1){
                                                                                                                                return 41;
                                                                                                                            }
                                                                                                                            if(steps == 2){
                                                                                                                                return 25;
                                                                                                                            }
                                                                                                                            if(steps == 3){
                                                                                                                                return 26;
                                                                                                                            }
                                                                                                                            if(steps == 4){
                                                                                                                                return 27;
                                                                                                                            }
                                                                                                                            if(steps == 5){
                                                                                                                                return 28;
                                                                                                                            }
                                                                                                                            if(startindex==31){
                                                                                                                                if(steps == 1){
                                                                                                                                    return 30;
                                                                                                                                }
                                                                                                                                if(startindex==32){
                                                                                                                                    if(steps == 1){
                                                                                                                                        return 31;
                                                                                                                                    }
                                                                                                                                    if(steps == 2){
                                                                                                                                        return 30;
                                                                                                                                    }
                                                                                                                                    if(startindex==5){
                                                                                                                                        if(steps == 1){
                                                                                                                                            return 33;
                                                                                                                                        }
                                                                                                                                        if(steps == 2){
                                                                                                                                            return 34;
                                                                                                                                        }
                                                                                                                                        if(steps == 3){
                                                                                                                                            return 32;
                                                                                                                                        }
                                                                                                                                        if(steps == 4){
                                                                                                                                            return 42;
                                                                                                                                        }
                                                                                                                                        if(steps == 5){
                                                                                                                                            return 41;
                                                                                                                                        }
                                                                                                                                        if(startindex==10){
                                                                                                                                            if(steps == 1){
                                                                                                                                                return 35;
                                                                                                                                            }
                                                                                                                                            if(steps == 2){
                                                                                                                                                return 36;
                                                                                                                                            }
                                                                                                                                            if(steps == 3){
                                                                                                                                                return 32;
                                                                                                                                            }
                                                                                                                                            if(steps == 4){
                                                                                                                                                return 42;
                                                                                                                                            }
                                                                                                                                            if(steps == 5){
                                                                                                                                                return 41;
                                                                                                                                            }
                                                                                                                                            if(startindex==15){
                                                                                                                                                if(steps == 1){
                                                                                                                                                    return 37;
                                                                                                                                                }
                                                                                                                                                if(steps == 2){
                                                                                                                                                    return 38;
                                                                                                                                                }
                                                                                                                                                if(steps == 3){
                                                                                                                                                    return 32;
                                                                                                                                                }
                                                                                                                                                if(steps == 4){
                                                                                                                                                    return 42;
                                                                                                                                                }
                                                                                                                                                if(steps == 5){
                                                                                                                                                    return 41;
                                                                                                                                                }
        }
    private static int followPath(int startindex, int steps, int previousIndex, int style) {
        if (style == 4) {
            if (startindex == 22) {
                return previousIndex;
            } else if (startindex == 0) {
                return previousIndex;
            } else if (startindex == 15) {
                return previousIndex;
            } else if (startindex == 25) {
                return 10;
            } else if (startindex == 27) {
                return 28;
            } else if (startindex == 23) {
                return 5;
            } else if (startindex == 28 || startindex == 21) {
                return 22;
            } else if (startindex == 20) {
                return 21;
            } else return startindex - 1;
        } else if (style == 5) {
            if (startindex == 27) {
                return previousIndex;
            } else if (startindex == 0) {
                return previousIndex;
            } else if (startindex == 20) {
                return previousIndex;
            } else if (startindex == 28) {
                return 5;
            } else if (startindex == 30) {
                return 10;
            } else if (startindex == 32) {
                return 15;
            } else if (startindex == 35) {
                return 27;
            } else if (startindex == 34) {
                return 35;
            } else if (startindex == 26) {
                return 27;
            } else if (startindex == 25) {
                return 26;
            } else return startindex - 1;
        } else if (style == 6) {
            if (startindex == 32) {
                return previousIndex;
            } else if (startindex == 0) {
                return previousIndex;
            } else if (startindex == 25) {
                return previousIndex;
            } else if (startindex == 35) {
                return 10;
            } else if (startindex == 33) {
                return 5;
            } else if (startindex == 37) {
                return 15;
            } else if (startindex == 42) {
                return 32;
            } else if (startindex == 31) {
                return 32;
            } else return startindex - 1;
        }
    }
