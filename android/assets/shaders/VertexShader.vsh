float4x4 matWorldViewProj; 
float4x4 matWorld; 
float4 vecLightDir;
float4 vecEye;

struct VS_INPUT
{
	float4 Position: POSITION0;
	float2 TexCoord: TEXCOORD0;
	float3 Normal: NORMAL0;
};

struct VS_OUTPUT
{
	float4 Position : POSITION;
	float2 TexCoord : TEXCOORD0;
	float3 Normal : TEXCOORD1;
	float3 LightDir : TEXCOORD2;
	float3 ViewDir : TEXCOORD3;
};

VS_OUTPUT vs_main(in VS_INPUT In)
{
	VS_OUTPUT Out;
	 
	Out.Position = mul(In.Position, matWorldViewProj);		//Transform Position
	Out.TexCoord = In.TexCoord;								//Pass UVs along
	Out.Normal = normalize(mul(In.Normal, (float3x3)matWorld));	//Transform normal
	Out.LightDir = normalize(vecLightDir.xyz);				//Pass Light Direction to PS
	float3 PosWorld = normalize(mul(In.Position, matWorld));//Determine the world position 
	Out.ViewDir = normalize((vecEye - PosWorld).xyz);		//Get the difference from the eye.

	return Out;
}