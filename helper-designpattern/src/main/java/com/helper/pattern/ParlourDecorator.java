package com.helper.pattern;

import javax.swing.*;
import java.awt.*;

/**
 * 模式的应用场景
 * 建造者（Builder）模式创建的是复杂对象，其产品的各个部分经常面临着剧烈的变化，但将它们组合在一起的算法却相对稳定，所以它通常在以下场合使用。
 * 创建的对象较复杂，由多个部件构成，各部件面临着复杂的变化，但构件间的建造顺序是稳定的。
 * 创建复杂对象的算法独立于该对象的组成部分以及它们的装配方式，即产品的构建过程和最终的表示是独立的。
 * 模式的扩展
 * 建造者（Builder）模式在应用过程中可以根据需要改变，如果创建的产品种类只有一种，只需要一个具体建造者，这时可以省略掉抽象建造者，甚至可以省略掉指挥者角色。
 *
 * @author sz_qiuhf@163.com
 **/
public class ParlourDecorator {
    public static void main(String[] args) {
        try {
            Decorator d;
            d = (Decorator) ReadXml.getObject();
            ProjectManager m = new ProjectManager(d);
            Parlour p = m.decorate();
            p.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

/**
 * 产品：客厅
 */
class Parlour {
    /**
     * 墙
     */
    private String wall;
    /**
     * 电视
     */
    private String TV;
    /**
     * 沙发
     */
    private String sofa;

    void setWall(String wall) {
        this.wall = wall;
    }

    void setTV(String TV) {
        this.TV = TV;
    }

    void setSofa(String sofa) {
        this.sofa = sofa;
    }

    void show() {
        JFrame jf = new JFrame("建造者模式测试");
        Container contentPane = jf.getContentPane();
        JPanel p = new JPanel();
        JScrollPane sp = new JScrollPane(p);
        String parlour = wall + TV + sofa;
        JLabel l = new JLabel(new ImageIcon("src/" + parlour + ".jpg"));
        p.setLayout(new GridLayout(1, 1));
        p.setBorder(BorderFactory.createTitledBorder("客厅"));
        p.add(l);
        contentPane.add(sp, BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

/**
 * 抽象建造者：装修工人
 */
abstract class Decorator {
    /**
     * 创建产品对象
     */
    Parlour product = new Parlour();

    public abstract void buildWall();

    public abstract void buildTV();

    public abstract void buildSofa();

    /**
     * 返回产品对象
     *
     * @return Parlour
     */
    Parlour getResult() {
        return product;
    }
}

/**
 * 具体建造者：具体装修工人1
 */
class ConcreteDecorator1 extends Decorator {
    @Override
    public void buildWall() {
        product.setWall("w1");
    }

    @Override
    public void buildTV() {
        product.setTV("TV1");
    }

    @Override
    public void buildSofa() {
        product.setSofa("sf1");
    }
}

/**
 * 具体建造者：具体装修工人2
 */
class ConcreteDecorator2 extends Decorator {
    @Override
    public void buildWall() {
        product.setWall("w2");
    }

    @Override
    public void buildTV() {
        product.setTV("TV2");
    }

    @Override
    public void buildSofa() {
        product.setSofa("sf2");
    }
}

/**
 * 指挥者：项目经理
 */
class ProjectManager {
    private Decorator builder;

    ProjectManager(Decorator builder) {
        this.builder = builder;
    }

    /**
     * 产品构建与组装方法
     *
     * @return Parlour
     */
    Parlour decorate() {
        builder.buildWall();
        builder.buildTV();
        builder.buildSofa();
        return builder.getResult();
    }
}
