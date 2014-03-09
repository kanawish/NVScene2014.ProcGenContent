//
//  Shader.fsh
//  ShaderBox
//
//  Created by Etienne Caron on 2/13/2014.
//  Copyright (c) 2014 Etienne Caron. All rights reserved.
//

varying lowp vec4 colorVarying;

void main()
{
    gl_FragColor = colorVarying;
}
