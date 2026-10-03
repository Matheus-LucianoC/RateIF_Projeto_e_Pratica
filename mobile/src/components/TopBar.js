import React from 'react';
import {StyleSheet,Text,View} from 'react-native';
import {COLORS} from '../theme';
export function TopBar({title,subtitle,usuario}){return <View style={styles.wrap}><View style={{flex:1}}><Text style={styles.kicker}>{subtitle||'RATEIF'}</Text><Text style={styles.title}>{title}</Text></View><View style={styles.avatar}><Text style={styles.avatarText}>{(usuario?.nome||'U').charAt(0).toUpperCase()}</Text></View></View>}
const styles=StyleSheet.create({wrap:{flexDirection:'row',alignItems:'center',justifyContent:'space-between',marginBottom:16},kicker:{color:COLORS.purple,fontSize:10,fontWeight:'900',letterSpacing:1.1},title:{color:COLORS.ink,fontSize:26,fontWeight:'900',marginTop:4},avatar:{width:42,height:42,borderRadius:12,backgroundColor:COLORS.primary,alignItems:'center',justifyContent:'center'},avatarText:{color:'#fff',fontSize:18,fontWeight:'900'}});
