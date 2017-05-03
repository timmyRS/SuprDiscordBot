var ServerSocket = Java.type("java.net.ServerSocket"),
BufferedReader = Java.type("java.io.BufferedReader"),
InputStreamReader = Java.type("java.io.InputStreamReader"),
DataOutputStream = Java.type("java.io.DataOutputStream");

var socket, running = true;

script.on("LOAD", function()
{
	socket = new ServerSocket(80);
	while(running)
	{
		var client = socket.accept();
		var inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
		var str;
		while((str = inFromClient.readLine()) != null)
		{
			if(str.substr(0, 5) == "GET /")
			{
				if(str.substr(str.length - 9) == " HTTP/1.1")
				{
					var outToClient = new DataOutputStream(client.getOutputStream());
					var path = str.split(" ")[1];
					var arr = handleRequest(path);
					outToClient.writeBytes("HTTP/1.1 " + arr[0] + "\nConnection: keep-alive\nContent-Length: " + arr[1].length() + "\nContent-Type: " + arr[2] + "\n\n" + arr[1] + "\n");
					outToClient.flush();
				}
			}
		}
	}
	socket.close();
}).on("UNLOAD", function()
{
	running = false;
})

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
