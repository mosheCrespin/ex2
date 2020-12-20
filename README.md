#ex2
**this project is assighment number 3 of the course Object Oriented programming in Ariel University **
//////////////////////////////////////////////////////////////////////////////////////////////////////

**how to start?**
first clone this repository using this following command: `git clone https://github.com/mosheCrespin/ex2.git`






**first info:**
*there are 2 packages in this project: `api` and `gameClient`. 
inside `api` package there are 5 classes: `NodeData`, `EdgeData`, `geoLocation`, `DWGraph_DS` and `DWGraph_Algo`
inside gameClient there is 1 package called util that contaims 4 classes and another 8 classes.
`util` has 4 classes named `Point3D`, `Range`, `Range2D`, `Range2Range`.
`gameClient` also contains anothr 7 classes: Arena, CL_Agent, CL_Pokemon, Ex2 ,MyLoginPage ,MyPanel and AgentManaging.

**put attention that you can find the Algorithm of the game in Line 213 under the explanation of AgentsManaging class**

//////////////////////////////////////////////////////////////////////////////////////////////////////
**classes of api packege: **
**NodeData**

*this class is public. Each node has 5 instance variables: `Key`-a unique id for this node. `info`- the information that the node holds,
 `tag`- usually used for the algorithm class,`whight`-weight associated with this node and `location`-hold the location of the node;

*In this class there are getters and setters(there is no setter for key) and there is `toString()` method and `NodeData` constructor that get `node_data`;

-------------------------------
**EdgeData**

*this class is public represent a connection between nodes in the graph,all edges are one direction:`src`-the node that the edge start from ,
`dest`-the destination of the edge,`whight`-the weight of the age`info`-meta data associated with this edge and `tag``EdgeData`-
*In this class there are getters and setters(there is no setter for src and dest) and there is `toString()` method and `EdgeData` constractor ;

-------------------------------
**geoLocation**

*this class is public represent a location point using x,y,z :in this class there is `geoLocation` constructor that get x,y,z, and function that 
get geo_location and return the distance between this location and the given location.
*In this class there are getters and `geoLocation` constractor ;
 
--------------------------------
**WGraph_DS**

*this class is public represents directed (positive) Weighted Graph. this class holds Nodedata as inner class. each object of this class contains 
a data structure who holds the nodes of the graph(in Hash-Map). in addition, there are 3 instance variables: `nodeSize`- the amount of nodes in the 
graph, `edgeSize`- the amount of edges in the graph, `amountOfChanges`- the amount of changes made on the graph;
 
*In this class there are getters for the Instance variables and `toString()` method;

*the `addNode(key)` method adds the node with the given id to the graph;

*the `connect(key1,key2,weight)` method connect between the given nodes. 

*the `getnode(int key)` method that returns the node_data by the node_id. 

*the `getEdge(node1,node2) method check if there is an edge between src to dest given nodes, if not the method returns -1. if the answer is yes then
 the method returns the weight between the given nodes(the weight between node to itself is 0);

*the `removeNode(key)` method removes the node from the graph and all of his edges that connect to him,and return the node if he exist;

*the `removeEdge(key1,key2)method to check if there is an edge between the src given node to the dest given node. if the answer is yes then the
 method removes the edge between the given nodes. else the answer is no, so the method do nothing,the function return the edge if he existed;

*the `getV()` method returns a Collection of all the nodes in the graph;

*the `getE(int node_id)`method returns a collection representing all the edges getting out of  the given node.this method returns a Collection of 
all the nodes in the graph;

*the `nodeSize()`method returns a number of nodes in the graph.

*the `edgeSize()`method returns a number of edges in the graph.

*the `getMC()`method returns a number of changes that occur in the graph.

*In this class there is a `DWGraph_DS()` constructor for build a new directed_weighted_graph.

------------------------------------

**DWGraph_Algo**

*this class represents the Theory algorithms for an directed weighted graph;

*the `init(directed_weighted_graph g)` method Initializing the graph of this class to work with the given graph;

*the `getGraph()` method simply returns the graph;

*the `copy() method returns an all new graph (deep-copy);

*the `isConnected()` method returns true if the graph is connected ;

*the `shortestPathDist(src, dest)` method returns the length of the shortest path between `src` to `dest` via weight using Dijkstra's algorithm. 
if there is no path then the method returns -1. if src is equal to dest then the method returns 0;

*the `shortestPath(src, dest)` method, this method using Dijkstra's algorithm. it returns an actual path between `src` to `dest` via List of node_info. if `src` or `dest` are not nodes in the graph, than the method returns `null`. if `src` is equal to `dest` then the method returns a list with only 
the node who belongs to`src`. if there is no path between the nodes, then the method returns an empty list; 

*the `save(dest)` method simply save the graph using `java.io.Serializable` with the name of `dest`, the method returns `true` or `false` depends on 
if the process succeeded;

*the `load(dest)` method simply load the `dest` file using `java.io.Serializable` the loaded graph will be the class graph, if there is no such
 file(dest) in the memory then the graph will remain with no differences. the method returns `true` or `false` depends on if the process succeeded;

*In this class there is a inner class name 'graphJsonDeserializer' that have 'directed_weighted_graph' function that get jesons make a new graph 
and deserialize the information to the new graph.  

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

**classes of package gameClient**

**Arena**

**his class represent the arena of the game includig the graph, pokemons and agents**
 
*the `setPokemons(String json)` method get a Json and and convert it to a List of pokemons including update their edges
 
*the `setNumberOfAgents(String json)` method get a Json and update the number of agents
 
*the `setInfo (String json)` method get a Json and update the info about this game (the Json came from the server of the game)
 
*the `updateInfo(String json, int timeLeft )` method get Json String and timeLeft  this method updates the info for the curr time
 
*the `json2Pokemons(String fs)` method get a Json and convert it to a Pokemon List 
 
 *the `updateEdge(CL_Pokemon fr, directed_weighted_graph g)` get a pokemon and find which edge belongs to this pokemon , this method has 3 more helpful methods that has the same name (`isOnEdge()`) these methods helps to find this edge 
 
the `GraphRange(directed_weighted_graph g)` method get the graph and check what is the range of this graph

the `w2f(directed_weighted_graph g, Range2D frame)` method convert from the graph range to the frame range. this method useing `GraphRange()` method to find the range of the graph.

the `LoadGraphFromJson(String str)` gets a Json represents a graph from the server, and convert it into a graph object using Gson and an inner class named `graphJsonDeserializer`.


-----------------------------------------------------------------
**CL_Agent**

*this class represent all the data that connected to a single agent of the game including his ID and the curr target(a pokemon).
 
 *this class has getters and setters and ofcours `toString` method
 *the `update(String json)` method get Json and update the agent information. also if this agent on the move this function will print to the screen where he is going

 ////////////////////////////////////////////////////
 
 **CL_Pokemon**
 
 *this class represent the data of the pokemons in the graph like uniqe id, edge, value, type, pos and there is a `Hashsets` called busy. 

 **some explenation about the hashset**
 every pokemon gets is own uniqe `id` that calculate like that- point is the location of the pokenon so do this calculation (point.x() + point.y() + v + t)
 once we have uniqe id for every pokemon we can add this pokemon to a HashSet that contains all the pokemons that on the aim of some agent.
 
 the `setIsBusy()` method gets boolean value, if true then add this pokemon to the Hashset else remove this pokemon from the Hashset
 
 the `isBusy()` method return if this pokemon is on the aim of some agent.
 
 *this class also has getters and setters and ofcourse `toString` method
 

 /////////////////////////////////////////////////////
 
 **MyLoginPage**
 
 *this class is responsible to the login page for the game.
 this class extends JFrame and implements ActionListener and MouseListener 
 in this page the user need to choose a level between 0 to 23,  also there is an optional field for adding an id number.
 *the constructor of this class responsible for how the login frame will look like.
 * 'actionPerformed(ActionEvent e)' this method check if the input that the user entered is legal, if so then the game will start with this input. otherwise the user will get notified that the input is not legal. 
 
 *the 'mousePressed(MouseEvent e)' method removes the "Optional" word from the text field connected to the ID.
 
 *the method `get_user_successfully_connected()` return boolean value represents if the user enterd legal input. if true then the game begins.
 
 *the other getters are responsible for delivering the input from this class to another class(in our case to `Ex2.java`)
 
 
 ////////////////////////////////////////
 
 **MyPanel**
 
 *this class is responsible to show the GUI of the game.

 *the 'updateFrame()' method declare whice size the frame will show.
 
 *the 'paintComponent(Graphics g)' method update the frame and draw the nodes edges agents pokemons Info and drawBG.
 
 *the 'drawBG(Graphics g)' method print to the prame of the page backGround .
 
 *the 'drawInfo(Graphics g)' method print in the frame page info about this game like whice level this game how much agents are and how much time left 
 in this gave .
 
 *the 'drawPokemons(Graphics g)' method print in the frame the pokemons with id and value of the pokmons.
 
 *the 'drawAgants(Graphics g)' method print the agents in the frame page.
 
 *the 'drawEdges(Graphics g)' method run over all the nodes in the graph and run over all there edges this method call drawEdge(edge_data e, Graphics g) 
 that print the edges to the frame.
 
 *the 'drawEdge(edge_data e, Graphics g) ' method print the edges in the frame.
 
 *the 'drawNodes(Graphics g)' method run over all the nodes in the graph.
 
 *the 'drawNode(node_data n, int r, Graphics g)' method print all the nodes to the frame.
 *there is also an exit buttom to end this game.
 
/////////////////////////////

**AgentsManaging**
*this class is responsible for the Algorithm of each agent. 
the `run` method is responible for the algorithm of each agent.
**the algorithm**
     while the game is running do these steps for each agent:
     * 1. check where is the best pokemon for this agent is allocate(call to `whereShouldIGo()` method.
     * 1.1 the best pokemon is the one that has the biggest value and the smallest path
     * 2. now there is a target for this agent so start to do these simple things:
     * 3. while the game is running there are 2 possible options:
     * 3.1 this agent still has the old pokemon so when the agent is not moving do these things:
     * 3.1.1 first tell the server to move forward the pokemon (one node for every call)
     * 3.2 this agent just ate his pokemon so do these steps:
     * 3.2.1 wait until this agent will eat the pokemon
     * 3.2.2 find a new pokemon to eat (using `whereShouldIGo` method) 
     * 3.2.3 check if there is another pokemon on this edge (using 'isTherePokemonInThisEdge()' method) if so then eat this Pokemon too
     * 3.2.4 tell the server to move forward the new pokemon
     
 * the 'isTherePokemonInThisEdge(int src, int dest)' method run over all the pokemons and check if this pokemons is on this edge.
 
 * the 'timeToGetToPokemon(CL_Pokemon pokemon)' method return the time it will take to the agent to get to the pokemon.
 
 * the 'value(CL_Pokemon pokemon)' method return the value of the pokemons this method calculate (the return value of `time to get to the pokemon()` / the value of this pokemon  
 * the 'whereShouldIGo()' method run over all the pokemons that existent in this moment and check whice of them is closest and also free .the 
 method say to the agent to go to this pokemon. 
 
 * the 'updateAgent(String json)' method updates the agent info from a Json.
 
 * the 'strike()' method check if the agent try to eat this pokemon 3 time and didn't succeed if so then tell him to run over another pokemon.
  
 ////////////////////////////////
 **Ex2**
 *there are 2 options for the start of this game:
 if the game starts from the Command Line with input of level and ID (Optional) then the game starts with that input.
 otherwise an entrence frame will pop up and wait wo the user to enter the level he wants and his ID(Optional)
*the 'run()' method is Responsible for managing the game, this includes to ןnitialize the game, refreshing the frame(GUI) and tell the server to move(game.move()) in a constant time of 100 ms.

*the 'entrancePage()' method shows the login panel until the user succeed to enter a valid input

*the 'init(game_service game) method init all the information about the game like arena ,setpokemons ,numberofagnts and also make the frame for 
the GUI of the game.
	
*the 'distnaceArr()'method run over all the nodes in the graph and calculate the distance to all other nodes in the graph, this information
keeps in the distance matrix (2D).
	
*the 'not_a_trap()' method check whice node in the graph is in a deadlock if so then distance[i][j]==distance[j][i]==-1.
	
*the 'pathArr(int nodeSize)' run over all the nodes in the graph and keeps all the shortest pathes in a matrix of lists.
	
