TERMUX_PKG_HOMEPAGE=https://github.com/jgm/cmark
TERMUX_PKG_DESCRIPTION="CommonMark parsing and rendering program"
TERMUX_PKG_VERSION=0.28.3
TERMUX_PKG_SHA256=acc98685d3c1b515ff787ac7c994188dadaf28a2d700c10c1221da4199bae1fc
TERMUX_PKG_SRCURL=https://github.com/jgm/cmark/archive/${TERMUX_PKG_VERSION}.tar.gz
TERMUX_PKG_EXTRA_CONFIGURE_ARGS="-DCMAKE_INSTALL_LIBDIR=$TERMUX_PREFIX/lib"