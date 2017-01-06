/**
 * @author Administrator
 * @since	2009-10-26
 * @describtion	序列化对象为JSON格式 遵循JSON组织公布标准 
 */
package com.commons.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;


public class Json {
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
	
	private static SimpleDateFormat DateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
	/**
	/**
	 * 任意对象转换成JSON对象
	 * @param obj	转换前的对象
	 * @return		转换后的对象
	 */
	public static String object2json( Object obj )
	{
        StringBuffer json = new StringBuffer("");
        if ( obj == null )
        {
            json.append( "\"\"" );
        }
        else if ( obj instanceof Integer || obj instanceof Float || obj instanceof Boolean
                || obj instanceof Short || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal
                || obj instanceof BigInteger || obj instanceof Byte ) 
        {
            json.append( "\"" ).append( string2json( obj.toString() ) ).append( "\"" );
        }
        else if( obj instanceof Date )
        {
        	
        	Date date = ( Date )obj;
        	String sdate = simpleDateFormat.format( date );
        	json.append( "\"" ).append( string2json( sdate ) ).append( "\"" );
        }
        else if ( obj instanceof Object[] ) 
        {
            json.append( array2json( ( Object[]) obj ) );
        } 
        else if ( obj instanceof List ) 
        {
            json.append( list2json( ( List ) obj) );
        } 
        else if ( obj instanceof Map ) 
        {
            json.append(map2json((Map) obj));
        } 
        else if (obj instanceof Set) 
        {
            json.append(set2json((Set) obj));
        } 
        else if ( obj instanceof String )
        {
        	String objstr = StringUtil.changToChinese( obj.toString() );
        	json.append("\"").append(string2json( objstr ) ).append("\"");
        }
        else 
        {
            json.append( bean2json(obj) );
        }
        return json.toString();
    }
	
	/**
	 * BEAN对象转换成JSON对象
	 * @param bean	BEAN对象
	 * @return		JSON对象
	 */
    public static String bean2json( Object bean ) 
    {
        StringBuffer json = new StringBuffer("");
        json.append( "{" );
        PropertyDescriptor[] props = null;
        try 
        {
            props = Introspector.getBeanInfo( bean.getClass(), Object.class ).getPropertyDescriptors();
        } 
        catch (IntrospectionException e) 
        {
        }
        
        if ( props != null ) 
        {
            for( int i = 0; i < props.length; i++ ) 
            {
            	try 
            	{
            		String name = object2json( unCamelize( props[i].getName() ) );
            		String value = "\"\"";    
                      if(name.equals("\"SYS_MENU\""))
                        {                         
                    	   value = "\"\"";                   	    
                        }
                      else{
                          value = object2json( props[i].getReadMethod().invoke(bean, null) );
                      }
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } 
            	catch (Exception e) 
            	{
                }
            }
            json.setCharAt( json.length() - 1, '}' );
        } 
        else 
        {
            json.append("}");
        }
        return json.toString();
    }
    
    /**
     * LIST转换成JSON对象
     * @param list	转换前的LIST对象
     * @return		转换后的JSON对象
     */
    public static String list2json( List list ) 
    {
    	StringBuffer json = new StringBuffer("");
        json.append("{");
        json.append("\"rows\":"+list.size()+",");
        json.append("\"data\":");
        json.append("[");
        if ( list != null && list.size() > 0 ) 
        {
        	for( int i = 0; i < list.size(); i++ )
        	{
        		Object obj = list.get( i );
        		json.append( object2json(obj) );
                json.append(",");
        	}
            json.setCharAt(json.length() - 1, ']');
            json.append("}");
        } 
        else 
        {
            json.append("]}");
        }
        return json.toString();
    }
    
    /**
     * 数组转换成JSON对象
     * @param array		转换前的数组
     * @return			转换后的JSON对象
     */
    public static String array2json( Object[] array ) 
    {
        StringBuffer json = new StringBuffer("");
        json.append("{");
        json.append("\"rows\":"+array.length+",");
        json.append("\"data\":");
        json.append("[");
        if ( array != null && array.length > 0 ) 
        {
        	for( int i = 0; i < array.length; i++ )
        	{
        		Object obj = array[i];
        		json.append( object2json( obj ) );
                json.append( "," );
        	}
            json.setCharAt( json.length() - 1, ']' );
            json.append( "}" );
        }
        else 
        {
            json.append( "]}" );
        }
        return json.toString();
    }
    
    /**
     * MAP对象转换成JSON对象
     * @param map	转换前的MAP对象
     * @return		转换后的JSON对象
     */
    public static String map2json(Map map) 
    {
        StringBuffer json = new StringBuffer("");
        json.append("{");
        if ( map != null && map.size() > 0 ) 
        {
        	Iterator iterator = map.keySet().iterator();
        	while( iterator.hasNext() )
        	{
        		Object key = iterator.next();
        		json.append( object2json( key ) );
	            json.append( ":" );
	            json.append( object2json( map.get( key ) ) );
	            json.append(",");
        	}
            json.setCharAt( json.length() - 1, '}' );
        } 
        else 
        {
            json.append( "}" );
        }
        return json.toString();
    }
    
    /**
     * 把SET对象转换为JSON对象
     * @param set	转换前的SET对象
     * @return		转换后的JSON对象
     */
    public static String set2json( Set set ) 
    {
    	StringBuffer json = new StringBuffer("");
        json.append("[]");
        return json.toString();
    }
    
    /**
     * 将字符型对象转换成JSON对象
     * @param s		转换前的字符串
     * @return		转换后的JSON对象
     */
    public static String string2json( String s ) 
    {
        if ( s == null || s.trim().equals( "" ) )
            return "";
        StringBuffer sb = new StringBuffer("");
        for ( int i = 0; i < s.trim().length(); i++ ) 
        {
            char ch = s.charAt(i);
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                if (ch >= '\u0000' && ch <= '\u001F') {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int k = 0; k < 4 - ss.length(); k++) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }
    
    /**
     * 转换字符串为大写字符串
     * @param str	转换前的字符串
     * @return		转换后的字符串
     */
    public static String unCamelize( String str )
    {
		StringBuffer sb = new StringBuffer();
		char[] c = str.toCharArray();
			
		for( int j = 0; j<c.length; j++ )
		{
			if( j == 0 ) 
			{
				sb.append( Character.toUpperCase( c[j] ) );
			}
			else
			{	
				if( Character.isUpperCase( c[j] ) )
				{
					sb.append("_");
					sb.append(c[j]);
				}
				else
				{
					sb.append(Character.toUpperCase(c[j]));
				}
			}
		}
		
		return sb.toString();
	}
    
    /**
	 * 任意对象转换成JSON对象
	 * @param obj	转换前的对象
	 * @return		转换后的对象
	 */
	public static String object2json2( Object obj ) 
	{
        StringBuffer json = new StringBuffer("");
        if ( obj == null ) 
        {
            json.append( "\"\"" );
        } 
        else if ( obj instanceof Integer || obj instanceof Float || obj instanceof Boolean
                || obj instanceof Short || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal
                || obj instanceof BigInteger || obj instanceof Byte ) 
        {
            json.append( "\"" ).append( string2json( obj.toString() ) ).append( "\"" );
        }
        else if( obj instanceof Date )
        {
        	
        	Date date = ( Date )obj;
        	String sdate = DateFormat.format( date );
        	json.append( "\"" ).append( string2json( sdate ) ).append( "\"" );
        }
        else if ( obj instanceof Object[] ) 
        {
            json.append( array2json( ( Object[]) obj ) );
        } 
        else if ( obj instanceof List ) 
        {
            json.append( list2json( ( List ) obj) );
        } 
        else if ( obj instanceof Map ) 
        {
            json.append(map2json((Map) obj));
        } 
        else if (obj instanceof Set) 
        {
            json.append(set2json((Set) obj));
        } 
        else if ( obj instanceof String )
        {
        	String objstr = StringUtil.changToChinese( obj.toString() );
        	json.append("\"").append(string2json( objstr ) ).append("\"");
        }
        else 
        {
            json.append( bean2json2(obj) );
        }
        return json.toString();
    }
	
	/**
	 * BEAN对象转换成JSON对象
	 * @param bean	BEAN对象
	 * @return		JSON对象
	 */
    public static String bean2json2( Object bean ) 
    {
        StringBuffer json = new StringBuffer("");
        json.append( "{" );
        PropertyDescriptor[] props = null;
        try 
        {
            props = Introspector.getBeanInfo( bean.getClass(), Object.class ).getPropertyDescriptors();
        } 
        catch (IntrospectionException e) 
        {
        }
        
        if ( props != null ) 
        {
            for( int i = 0; i < props.length; i++ ) 
            {
            	try 
            	{
            		String name = object2json2( unCamelize( props[i].getName() ) );
            		String value = "\"\"";    
                      if(name.equals("\"SYS_MENU\""))
                        {                         
                    	   value = "\"\"";                   	    
                        }
                      else{
                          value = object2json2( props[i].getReadMethod().invoke(bean, null) );
                      }
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } 
            	catch (Exception e) 
            	{
                }
            }
            json.setCharAt( json.length() - 1, '}' );
        } 
        else 
        {
            json.append("}");
        }
        return json.toString();
    } 
}
