uniform mat4 uMVPMatrix;
attribute vec4 vPosition;

// Test comment
void main() {
	// The matrix must be included as a modifier of gl_Position.
	// Also, the uMVPMatrix factor *must be first* in order
	// for the matrix multiplication product to be correct.
	gl_Position = uMVPMatrix * vPosition;
}
