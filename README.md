```markdown
# Java Socket Game

This is a simple Java Socket-based game where clients connect to a server and try to guess a secret value. The server changes the secret keeper every minute, and clients attempt to guess the secret value.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed on your machine.
- An integrated development environment (IDE) such as Eclipse or IntelliJ.

### Running the Server

1. Clone this repository to your local machine.

   ```bash
   git clone https://github.com/<Lamia2428>/<guess_the_number_client_server_Socket>.git
   ```

2. Open the project in your preferred IDE.

3. Locate the `ServerMT.java` file in the `src` directory.

4. Run the `main` method in `ServerMT.java`.

   This will start the server, and it will listen for client connections on port `1234`.

### Running the Client

1. Open a terminal or command prompt.

2. Navigate to the project directory.

   ```bash
   cd /path/to/<guess_the_number_client_server_Socket>
   ```

3. Locate the `Repartiteur.java` file in the `src` directory.

4. Compile the `Repartiteur.java` file.

   ```bash
   javac Repartiteur.java
   ```

5. Run the compiled `Repartiteur` class, providing the server's IP address as a command-line argument.

   ```bash
   java Repartiteur <server_ip_address>
   ```

   Replace `<server_ip_address>` with the actual IP address of the machine running the server.

6. Follow the on-screen instructions to play the game.
7. Telnet
    If you prefer testing with Telnet, follow these steps:

Open a terminal or command prompt.

Connect to the server using Telnet.
 telnet <server_ip_address> 1234

## Contributing

If you'd like to contribute to this project, feel free to fork the repository, make your changes, and submit a pull request.


```
