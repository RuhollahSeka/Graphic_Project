#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 textureCoords;
layout (location = 2) in vec3 normals;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 diffuse;

out vec2 pass_textureCoords;
out vec3 pass_normals;
out vec3 pass_posAfterTransformation;
out vec3 diffuseColor;

void main()
{
//    pass_normals = mat3(transpose(inverse(transformationMatrix))) * normals;
    pass_normals = mat3(transformationMatrix) * normals;
    pass_posAfterTransformation = vec3(transformationMatrix * vec4(position, 1.0));
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    pass_textureCoords = textureCoords;
    diffuseColor = diffuse;
}