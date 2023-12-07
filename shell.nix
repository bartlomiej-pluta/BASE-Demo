{ pkgs ? import <nixpkgs> {} }:

# Because of high dependency on native libraries like OpenGL and OpenAL
# on the NixOS the BASE engine requires few modifications.
# Basically this shell.nix could be enough, however the OpenGL is highly tied with the hardware,
# so the following change needs to be applied in the configuration.nix (or your flake configuration, whatever):
#  hardware.opengl = {
#    enable = true;
#    extraPackages = with pkgs; [
#      libGL
#    ];
#    setLdLibraryPath = true;
#  };
#
# Also for GNOME environments, the Editor requires the libglassgtk3.so library,
# so it is available via xorg.libXtst package mentioned already in the shell definition below.

let
  opengl = "/run/opengl-driver";
in pkgs.mkShell {
  name = "BASE NixOS runtime";

  buildInputs = [
    pkgs.xorg.libXtst  # Required by editor on GNOME environments (due to libglassgtk3.so dependency)
    pkgs.alsa-lib      # Either alsa or jack or pulseaudio library is required by game engine via OpenAL native library
  ];

  # OpenGL needs to be configured via /etc/nixos/configuration.nix
  LD_LIBRARY_PATH = "${opengl}/lib:${pkgs.xorg.libXtst}/lib:${pkgs.alsa-lib}/lib";

  shellHook = ''
    echo
    echo
    echo "======================================================================================================"
    echo "Welcome to BASE NixOS shell environment"
    echo "Remember to provide following LD environment variable in order to run application outside this shell:"
    echo
    echo "LD_LIBRARY_PATH=$LD_LIBRARY_PATH"
    echo "======================================================================================================"
    echo
    echo
  '';
}
