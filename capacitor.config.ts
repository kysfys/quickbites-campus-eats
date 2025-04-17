
import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'app.lovable.be9ca838b4da403187128ec1ad7d2877',
  appName: 'quickbites-campus-eats',
  webDir: 'dist',
  bundledWebRuntime: false,
  server: {
    url: "https://be9ca838-b4da-4031-8712-8ec1ad7d2877.lovableproject.com?forceHideBadge=true",
    cleartext: true
  },
};

export default config;
