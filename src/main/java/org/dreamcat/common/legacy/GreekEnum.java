package org.dreamcat.common.legacy;

enum GreekEnum {
    Alpha(1), Beta(2), Gamma(3), Delte(4),
    Epsilon(5), Zeta(6), Eta(7), Theta(8),
    Iota(9), Kappa(10), Lambda(11), Mu(12),
    Nu(13), Xi(14), Omicron(15), Pi(16),
    Rho(17), Sigma(18), Tau(19), Upsilon(20),
    Phi(21), Chi(22), Psi(23), Omega(24);

    private int value;

    GreekEnum(int v) {
        value = v;
    }

    public char up() {
        if (value >= 18) return (char) (913 + value);
        else return (char) (913 + value - 1);
    }

    public char low() {
        if (value >= 18) return (char) (945 + value);
        else return (char) (945 + value - 1);
    }

    public int val() {
        return value;
    }
}

