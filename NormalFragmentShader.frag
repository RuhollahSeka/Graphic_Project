#version 400 core

//in vec3 vertexColor;
in vec2 pass_textureCoords;
in vec3 pass_normals;
in vec3 pass_posAfterTransformation;
in vec3 diffuseColor;

uniform sampler2D textureSampler;

vec3 diffusePosition = vec3(0.0, 1.0, 0.0);
vec3 ambientColor = vec3(1.0, 1.0, 1.0);
//vec3 diffuseColor = vec3(1.0, 1.0, 1.0);
float ambientEffect = 0.1;


out vec4 out_Color;

void main()
{
    vec3 diffuseDirection = normalize(diffusePosition - pass_posAfterTransformation);
    vec3 normalizedNormal = normalize(pass_normals);
    float diffuseEffect = max(dot(normalizedNormal, diffuseDirection), 0.0);
    vec3 diffuse = diffuseEffect * diffuseColor;
    vec3 ambient = ambientEffect * ambientColor;
    vec3 overall = ambient + diffuse;
    out_Color = texture(textureSampler, pass_textureCoords) * vec4(overall, 1.0);
}