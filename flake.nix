{
  description = "BASE Demo Game";

  inputs = {
    nixpkgs.url = github:NixOS/nixpkgs/nixos-23.11;
  };

  outputs = { self, nixpkgs }:
  let
    pkgs = import nixpkgs { inherit system; };
    system = "x86_64-linux";
    opengl = "/run/opengl-driver";
  in {
    devShells.${system}.default = with pkgs; mkShell {
      name = "base-demo";

      buildInputs = [
        xorg.libXtst
        alsa-lib
        jdk17
      ];

      shellHook = ''
        echo
        echo
        echo "======================================================================================================"
        echo "Welcome to BASE Demo Game NixOS flake shell environment"
        echo "Remember to provide following LD environment variable in order to run the game outside this shell"
        echo
        echo "LD_LIBRARY_PATH=$LD_LIBRARY_PATH"
        echo "======================================================================================================"
        echo
        echo
      '';

      LD_LIBRARY_PATH = "${opengl}/lib:${xorg.libXtst}/lib:${alsa-lib}/lib";
    };
  };
}
