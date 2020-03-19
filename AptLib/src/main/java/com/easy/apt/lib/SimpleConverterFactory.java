
package com.easy.apt.lib;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;


public class SimpleConverterFactory implements Converter.Factory {

    Converter<? extends Serializable, String> mFromSerializableConverter = new Converter<Serializable, String>() {
        @Override
        public String convert(Serializable value) {
            if (value == null) {
                return null;
            }
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream out = new ObjectOutputStream(byteOutputStream);
                out.writeObject(value);
                out.close();
                return Base64.encodeToString(byteOutputStream.toByteArray(), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    Converter<String, ? extends Serializable> mToSerializableConverter = new Converter<String, Serializable>() {
        @Override
        public Serializable convert(String value) {
            if (value == null) {
                return null;
            }
            try {
                ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(Base64.decode(value, Base64.DEFAULT)));
                return (Serializable) in.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    Converter<Parcelable, String> mFromParcelableConverter = new Converter<Parcelable, String>() {
        @Override
        public String convert(Parcelable value) {
            if (value == null) {
                return null;
            }
            Parcel parcel = Parcel.obtain();
            value.writeToParcel(parcel, 0);
            final byte[] marshall = parcel.marshall();
            parcel.recycle();
            return Base64.encodeToString(marshall, Base64.DEFAULT);
        }
    };

    class ToParcelableConverter<T> implements Converter<String, T> {

        public ToParcelableConverter(Parcelable.Creator<T> creator) {
            this.mCreator = creator;
        }

        Parcelable.Creator<T> mCreator;

        @Override
        public T convert(String value) {
            if (value == null) {
                return null;
            }
            final byte[] bytes = Base64.decode(value, Base64.DEFAULT);
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(bytes, 0, bytes.length);
            parcel.setDataPosition(0);
            return mCreator.createFromParcel(parcel);
        }
    }

    @Override
    public <F> Converter<F, String> fromType(Type fromType) {
        if (fromType instanceof Class) {
            Class fromClass = (Class) fromType;
            if (Serializable.class.isAssignableFrom(fromClass)) {
                return (Converter<F, String>) mFromSerializableConverter;
            }
            if (Parcelable.class.isAssignableFrom(fromClass)) {
                return (Converter<F, String>) mFromParcelableConverter;
            }
        }
        throw new IllegalArgumentException("SimpleConverterFactory supports only Serializable and Parcelable");
    }

    @Override
    public <T> Converter<String, T> toType(Type toType) {
        if (toType instanceof Class) {
            Class toClass = (Class) toType;
            if (Serializable.class.isAssignableFrom(toClass)) {
                return (Converter<String, T>) mToSerializableConverter;
            }
            if (Parcelable.class.isAssignableFrom(toClass)) {
                try {
                    return new ToParcelableConverter<T>((Parcelable.Creator<T>) toClass.getField("CREATOR").get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        throw new IllegalArgumentException("SimpleConverterFactory supports only Serializable and Parcelable");
    }
}
