// Webserver.js
// A very bad implementation of a HTTP server in JavaScript, interpreted by Nashorn.
// Copyright (c) 2017, timmyRS

var ServerSocket = Java.type("java.net.ServerSocket"), // This is basically importing.
BufferedReader = Java.type("java.io.BufferedReader"),
InputStreamReader = Java.type("java.io.InputStreamReader"),
DataOutputStream = Java.type("java.io.DataOutputStream"),
Thread = Java.type("java.lang.Thread");

var socket = null, running = false; // These are global variables

script.on("LOAD", function() // On load of the script...
{
    try { // We try to...
	    socket = new ServerSocket(80); // Open a socket on port 80
	    running = true;
	} catch(e) // if that fails, we just stop.
	{
        console.error("Couldn't bind to port 80. Webserver.js is now doing *nothing*!");
        return;
	}
	while(running) // We can while() as long as we want in LOAD, as every event has its own thread.
	{
		var client = socket.accept(); // Here we wait for an ingoing connection
		var inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
		var str;
		while((str = inFromClient.readLine()) != null) // Here we read each line sent by the client.
		{
			if(str.substr(0, 5) == "GET /" && str.substr(str.length - 9) == " HTTP/1.1") // If the line roughly represents a HTTP GET request...
			{
                var outToClient = new DataOutputStream(client.getOutputStream());
                var path = str.split(" ")[1];
                var arr = handleRequest(path); // Here we call handleRequest() with the requested path to find a fitting answer to send.
                outToClient.writeBytes("HTTP/1.1 " + arr[0] + "\nConnection: keep-alive\nContent-Length: " + arr[1].length() + "\nContent-Type: " + arr[2] + "\n\n" + arr[1] + "\n");
                outToClient.flush();
			}
		}
	}
	socket.close(); // If running is set to false, we just close the socket cleanly.
});

function handleRequest(path)
{
	switch(path)
	{
		default:
		return ["404", "404", "text/plain"];

		case "/":
		return ["200", "<h1>SuprDiscordBot</h1>", "text/html"];
	}
}

script.on("UNLOAD", function() // On unload of the script...
{
    if(running) // And when running is true...
    {
        running = false; // We set running to false, telling the socket to close.
        if(socket == null)
        {
            return; // Stop executing the unload function, as the socket is null.
        }
        while(!socket.isClosed()) // Whilst the socket is not closed...
        {
            Thread.sleep(500); // We sleep half a second.
        }
        socket = null; // Lastly, we set the socket to null, so the garbage collector knows this isn't required anymore.
    }
});
