import React from 'react';
import {StyleSheet,Text,TextInput,View} from 'react-native';
import {COLORS} from '../theme';
export function Input({label,...props}){return <View style={styles.container}><Text style={styles.label}>{label}</Text><TextInput {...props} style={styles.input} placeholderTextColor="#98A1B5" autoCapitalize={props.autoCapitalize??'none'}/></View>}
const styles=StyleSheet.create({container:{marginBottom:12},label:{color:'#4D5872',fontSize:12,marginBottom:7,fontWeight:'800'},input:{minHeight:48,borderRadius:12,borderWidth:1,borderColor:COLORS.line,backgroundColor:COLORS.surface,color:COLORS.ink,paddingHorizontal:14,fontSize:15}});
