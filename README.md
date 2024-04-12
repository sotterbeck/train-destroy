# TrainDestroy

![Train destory logo](docs/train-destroy-banner.png)
TrainDestroy is a powerful paper spigot plugin tailored for metro servers seeking enhanced performance. It efficiently
removes all minecarts in designated worlds at regular intervals, employing asynchronous scheduling to ensure optimal
server responsiveness. With customizable messages, intervals, and multilingual support, TrainDestroy offers a seamless
solution to streamline your server's performance management.

## Features

- **Efficient Minecart Removal:** Automatically removes all minecarts in designated worlds at scheduled intervals,
  preventing clutter and improving server performance.

- **Asynchronous Operation:** Utilizes asynchronous scheduling logic to execute tasks off the main server thread,
  ensuring minimal impact on server performance.

- **Configurability:** Customize TrainDestroy to suit your server's needs with configurable messages, intervals, and
  color schemes.

- **Multi-language Support:** Messages displayed in the player's preferred language (currently supports English and
  German).

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

3. Build the plugin JAR using Gradle

```
$ ./gradlew shadowJar
```

4. Locate the built JAR file in the build/libs directory.

## Usage

TrainDestroy provides the following commands:

- `/traindestroy status`: View performance information and schedule details.
- `/traindestroy enable`: Activate the TrainDestroy schedule.
- `/traindestroy disable`: Deactivate the TrainDestroy schedule.

## License

TrainDestroy is licensed under the Apache License 2.0. See the LICENSE file for details.