# IDSC

IDSC is an Expo-based Android utility for applying device identity profiles on rooted devices.

## What It Does

- Lets you choose a device profile from a bundled catalog
- Generates a shell script for applying that profile
- Updates device-related properties such as fingerprints, model metadata, build info, and visible device naming values
- Persists the generated script for reuse after reboot

## Stack

- Expo
- React Native
- Expo Router
- TypeScript

## Development

Install dependencies:

```bash
npm install
```

Start the project:

```bash
npm run start
```

Run on Android:

```bash
npm run android
```

## Notes

- This project targets rooted Android devices.
- The Android native project is committed in the repository.
- Review and test generated scripts carefully before using them on real devices.

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for details.
