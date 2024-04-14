# TrainDestroy

![Train destroy logo](docs/train-destroy-banner.png)

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

TrainDestroy is a powerful paper spigot plugin tailored for metro servers seeking enhanced performance. It efficiently
removes all minecarts in designated worlds at regular intervals, employing asynchronous scheduling to ensure optimal
server performance and reliability.

## Features

- **Efficient Minecart Removal:** Automatically removes all minecarts in designated worlds at scheduled intervals,
  preventing clutter and improving server performance.

- **Asynchronous Operation:** Utilizes asynchronous scheduling logic to execute tasks off the main server thread,
  ensuring minimal impact on server performance.

- **Configurability:** Customize TrainDestroy to suit your server's needs with configurable messages, intervals, and
  color schemes.

- **Multi-language Support:** Messages are being displayed in the player's preferred language (currently supports
  English and German).

> More features are being developed. TrainDestroy is currently a work-in-progress and is being tested in production on
> our Minecraft server.

## Building

To build TrainDestroy, follow these steps:

1. Clone the TrainDestroy repository to your local machine:

```
$ git clone https://github.com/sotterbeck/train-destroy.git
```

2. Navigate to the project directory:

```
$ cd train-destroy
```

3. Build the plugin JAR using Gradle:

```
$ ./gradlew shadowJar
```

Step 4: Locate the built JAR file in the `build/libs` directory, and choose the file that ends with `-all`.

## Usage

TrainDestroy ships with sensible defaults, so no further configuration is needed. However, if you want to customize
messages or scheduling intervals, edit `config.yml`. To adjust translation strings of messages refer to the files within
the `messages` directory.

TrainDestroy provides the following commands:

- `/traindestroy status`: View performance information and schedule details.
- `/traindestroy enable`: Activate the TrainDestroy schedule.
- `/traindestroy disable`: Deactivate the TrainDestroy schedule.

> The permissions directly correspond to the command names, meaning that the permission of the `/traindestroy status`
> command is `traindestroy.status`.

## License

TrainDestroy is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.