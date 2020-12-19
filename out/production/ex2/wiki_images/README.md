#ex2
*this project get from the server a directional weighted graph,number of agents and pokemons with location the algoritem need to
 eat the pokemons in shortest time. 
**info:**
*there are 2 packages in this project api and gameclient that we wrote in.in the api package there are 5 classes: NodeData,EdgeData,geoLocation,
 DWGraph_DS and DWGraph_Algo and in util there are 9 class
Arena,CL_Agent,CL_Pokemon,Ex2,moveMethod,MyJFrame,MyLoginPage,MyPaneland threadAgents .

**NodeData**

*this class is public. Each node has 5 instance variables: `Key`-a unique id for this node. `info`- the information that the node holds,
 `tag`- usually used for the algorithm class,`whight`-weight associated with this node and `location`-hold the location of the node;

*In this class there are getters and setters(there is no setter for key) and there is `toString()` method and `NodeData` constructor that get `node_data`;

////////////////////////////
**EdgeData**

*this class is public represent a connection between nodes in the graph,all edges are one direction:`src`-the node that the edge start from ,
`dest`-the destination of the edge,`whight`-the weight of the age`info`-meta data associated with this edge and `tag``EdgeData`-
*In this class there are getters and setters(there is no setter for src and dest) and there is `toString()` method and `EdgeData` constractor ;

**geoLocation**

*this class is public represent a location point using x,y,z :in this class there is `geoLocation` constructor that get x,y,z, and function that 
get geo_location and return the distance between this location and the given location.
*In this class there are getters and `geoLocation` constractor ;

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

///////////////////////////

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

/////////////////////////////////////////////////////////////////////////////////////////////

**Arena**

*this class represent the//?? 
 
 *the `setPokemons(String json)` method get Json String and update the pokemons in the graph in the pokemons location.
 
 *the `setNumberOfAgents(String json)` method get Json String and update the number of agents.
 
 *the `setInfo (String json)` method get Json String  and update the info about this game we get from the server.
 
 *the `updateInfo(String json, int timeLeft )` method get Json String and timeLeft  this method update the move,grads and time left.
 
 *the `json2Pokemons(String fs)` method get Json String and update pokemons in arraylist.
 
 *the `updateEdge(CL_Pokemon fr, directed_weighted_graph g)` method get String in Json and update the pokemons in the graph in the pokemons location.

*the `updateEdge(CL_Pokemon fr, directed_weighted_graph g)` method update the edge of he exist.

*the `graphJsonDeserializer implements JsonDeserializer<directed_weighted_graph>` method update the graph with the nodes and edges we get from the server.

////////

**CL_Agent**

*this class represent the data of the agents in the graph like agents location,id ,node they move from and node they go to and value.  
 
 *the `update(String json)` method get Json String and update the agents data.
 
 *the `setNextNode(int dest)` method get Json String and update the number of agents.
 
 *in this class there are alot of getters to get infromation about the agent like- `getLocation`,`getValue`,`getID`,`getNextNode`,`getSpeed `t,`get_curr_edge`,`isMoving` and`getSrcNode`.
 and setters like  `setSpeed` ,`set_curr_edge` ,`setCurrNode` ,`setNextNode`,`set_sg_dt`,`set_curr_fruit`.  
 and `toString` method .
 
 //////
 
 **CL_Pokemon**
 
 *this class represent the data of the pokemons in the graph like id,edge,value,type,pos,min_dist,min_ro also we have busy HashSet that get all the catch pokemons .    
 
 *In this class there are alot of getters like -` getId` ,` isBusy` ,` isStillFood` ,` get_edge` ,` getLocation` ,` getType` ,`getValue` . 
 
 *In this class there are 3 setters like-`set_edge(edge_data _edge)` ,`setIsStillFood(boolean flag)`, `setIsBusy(boolean flag)`.
 
 and `toString` method that print pokemons value and type  .
 
 //////////
 
 **MyJFrame**
 *this class is to do the frame page.
 ///////////
 
 **MyLoginPage**
 
 *this class is to show the login page for the game in this page the user need to choose a level from  0 to 23 ,
 if he want he can add his id  presss the buttom and the game start,there is a exit buttom to exit this page.
 
 *In this class there is a method call 'MyLoginPage()' that make the login page and declare how this page look like like where to out the buttom to 
 press the id and the level we want to play whice size the page will be and more.
 
 * 'actionPerformed(ActionEvent e)' this method check if the input that the user give is legal we over to the game page else he print the reson
 why this input is not good. 
 
 *the 'mousePressed(MouseEvent e)' method check if the detals the user press are good.
 
 *this method have gettrs like 'get_user_successfully_connected()',get_user_entered_id(),getId_num(),getLevel_num() .
 
 
 //////////
 
 **MyPanel**
 
 *this class is to show the game page.in this page show the graph with the agents run over the pokemons .
 there is a exit buttom to end this game.
 
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
 
///////////

**threadAgents**
 *the threadAgents(List<node_data>[][] path, double[][] distanceArr, DWGraph_Algo graph_algo, Arena arena, CL_Agent agent, game_service game)
 
 * the 'run()' method run all the time the game is running and check for the agents where to go to eat pokemons.
 
 * the 'isTherePokemonInThisEdge(int src, int dest)' method run over all the pokemons and check if this pokemons is on this edge.
 
 * the 'timeToGetToPokemon(CL_Pokemon pokemon)' method return the time it will take to the agent to get to the pokemon.
 
 * the 'value(CL_Pokemon pokemon)' method return the value of the pokemons this method calculate and return the (time to get to the pokemon) / (pokemon.getValue());  
 
 * the 'whereShouldIGo()' method run over all the pokemons that existent in this moment and check whice of them is closest and also free.the 
 method say to the agent to go to the this pokemon. 
 
 * the 'updateAgent(String json)' method update the agent from string json to CL_Agent.
 
 * the 'strike()' method check if the agent try to eat this pokemon 3 time and didn't succeed and tell him to run over another pokemon.
  
 ///////////
 **Ex2**
 *the 'main(String[] args)' method get from the user detail his id and level he choose of the game and if the detail are matching the game start.
 
	*the 'run()' method run all the time the game is running, this method check if the agent doesn't have agent to eat so the method told him whice node 
	to go.
	
	*the 'entrancePage()' method show the login panel until the user succeed to get to the game panel.
	
	*the 'init(game_service game) method init all the information about the game like arena ,setpokemons ,numberofagnts amd also make the frame for 
	the login panel.	
	
	*the 'distnaceArr()'method run over all the nodes in the graph and calculate the distance to all othe nodes in the graph this information
	keep in the distance array.
	
	*the 'not_a_trap()' method check whice node in the graph is in a deadlock.
	
	*the 'pathArr(int nodeSize)' method get number of nodes in the graph and update thr path[][] that contains the list nodes that need to over if the agent want to over from 
	one node to another.	
	
