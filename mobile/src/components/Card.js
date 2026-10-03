import React from 'react';
import {StyleSheet,Text,View} from 'react-native';
import {COLORS} from '../theme';
export function Card({title,value,description,accent=false}){return <View style={[styles.card,accent&&styles.accent]}><Text style={styles.title}>{title}</Text><Text style={styles.value}>{value}</Text><Text style={styles.description}>{description}</Text></View>}
const styles=StyleSheet.create({card:{flex:1,minHeight:122,minWidth:150,padding:16,margin:5,borderRadius:16,backgroundColor:COLORS.surface,borderWidth:1,borderColor:COLORS.line},accent:{backgroundColor:'#EEF1FF'},title:{color:COLORS.muted,fontSize:12,fontWeight:'800'},value:{color:COLORS.ink,fontSize:28,fontWeight:'900',marginTop:10},description:{color:'#8B94A8',fontSize:11,marginTop:4}});
