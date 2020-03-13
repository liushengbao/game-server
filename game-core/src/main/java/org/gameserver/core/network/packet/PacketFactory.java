package org.gameserver.core.network.packet;

import io.netty.buffer.ByteBuf;
import org.gameserver.core.network.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息包工厂
 *
 * @Author: shengbao
 * @Date: 2020/3/10 16:26
 */
public class PacketFactory implements BeanFactoryPostProcessor {

    static Logger logger = LoggerFactory.getLogger(PacketFactory.class);

    /**
     * 扫描路径
     **/
    List<String> locationPatterns = new ArrayList<>();

    /**
     * 请求扫描
     **/
    private static Map<Integer, Class<? extends BasePacket>> classMap = new HashMap<>();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        findClasses();
    }

    public void setPackge(List<String> packages) {
        for (String p : packages) {
            String locationPattern = "classpath*:" + p.replaceAll("\\.", "/") + "/**/*.class";
            locationPatterns.add(locationPattern);
        }
    }

    private List<Class<?>> findClasses() {
        try {
            List<Class<?>> classList = new ArrayList<>();
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (String locationPattern : locationPatterns) {
                Resource[] rs = resourcePatternResolver.getResources(locationPattern);
                for (Resource r : rs) {
                    MetadataReader reader = metadataReaderFactory.getMetadataReader(r);
                    AnnotationMetadata annotationMD = reader.getAnnotationMetadata();
                    if (annotationMD.hasAnnotation(Packet.class.getName())) {
                        ClassMetadata clazzMD = reader.getClassMetadata();
                        Class<? extends BasePacket> clazz = (Class<? extends BasePacket>) Class.forName(clazzMD.getClassName());
                        Packet annotation = clazz.getAnnotation(Packet.class);
                        if (annotation != null) {
                            classMap.put(annotation.value(), clazz);
                            logger.info("注册请求:{} class:{}", annotation.value(), clazz);
                        }

                    }
                }
            }
            return classList;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static Class<? extends BasePacket> getClass(int requestId) {
        Class<? extends BasePacket> clazz = classMap.get(requestId);
        return clazz;
    }

    public static BasePacket createPacket(Session session, ByteBuf byteBuf) {
        // 消息长度
        int msgLength = byteBuf.readInt();
        // 包id
        int packetId = byteBuf.readInt();

        Class<? extends BasePacket> clazz = getClass(packetId);
        try {
            BasePacket packet = clazz.newInstance();
            packet.setSession(session);
            packet.setPackId(packetId);
            packet.read(byteBuf);
            return packet;
        } catch (Throwable t) {
            logger.error("创建packet 异常!", t);
        }
        return null;
    }


}
