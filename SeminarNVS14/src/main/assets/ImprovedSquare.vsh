uniform mat4 uMVPMatrix;
attribute vec4 vPosition;

// Test comment
void main() {
	gl_Position = uMVPMatrix * vPosition;
}
