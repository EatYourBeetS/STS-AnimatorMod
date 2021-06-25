package eatyourbeets.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;
import eatyourbeets.interfaces.delegates.FuncT0;
import eatyourbeets.utilities.*;

import java.util.HashMap;
import java.util.Map;

public class EYBCombatScreen extends GUIElement
{
    protected final static FieldInfo<BobEffect> _bobEffect = JUtils.GetField("bobEffect", AbstractMonster.class);
    protected final static FieldInfo<Boolean> _isMultiDmg = JUtils.GetField("isMultiDmg", AbstractMonster.class);
    protected final static FieldInfo<Integer> _intentMultiAmt = JUtils.GetField("intentMultiAmt", AbstractMonster.class);

    protected final Map<AbstractMonster, FuncT0<ColoredString>> intents = new HashMap<>();
    protected final EYBCombatInfo combatInfo = new EYBCombatInfo();

    @Override
    public void Update()
    {
        //combatInfo.Update();
    }

    @Override
    public void Render(SpriteBatch sb)
    {
        //combatInfo.Render(sb);
    }

    public void AddSubIntent(AbstractMonster monster, FuncT0<ColoredString> calculateIntent)
    {
        intents.put(monster, calculateIntent);
    }

    public void RenderMonsterInfo(AbstractMonster m, SpriteBatch sb)
    {
        if (intents.containsKey(m))
        {
            RenderSubIntent(sb, m, m.intent, intents.get(m).Invoke());
            intents.remove(m);
        }
    }

    private void RenderSubIntent(SpriteBatch sb, AbstractMonster m, AbstractMonster.Intent intent, ColoredString damage)
    {
        if (intent == null || damage == null || damage.color == null)
        {
            return;
        }

        final boolean multiDamage = _isMultiDmg.Get(m);
        final int multiDamageAmount = _intentMultiAmt.Get(m);
        final BitmapFont font = EYBFontHelper.CardDescriptionFont_Large;
        final Color color = new Color(damage.color.r, damage.color.g, damage.color.b, m.intentAlpha);

        if (multiDamage)
        {
            FontHelper.renderFontLeftTopAligned(sb, font, damage.text + "x" + multiDamageAmount,
            m.intentHb.x + Scale(5), m.intentHb.cY + _bobEffect.Get(m).y - m.intentHb.height * 0.6f, color);
        }
        else
        {
            FontHelper.renderFontLeftTopAligned(sb, font, damage.text,
            m.intentHb.x + Scale(5), m.intentHb.cY + _bobEffect.Get(m).y - m.intentHb.height * 0.6f, color);
        }

        RenderHelpers.ResetFont(font);
    }
}
