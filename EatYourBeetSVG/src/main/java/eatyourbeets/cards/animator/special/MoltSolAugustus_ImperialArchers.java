package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.GATE.MoltSolAugustus;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class MoltSolAugustus_ImperialArchers extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MoltSolAugustus_ImperialArchers.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(MoltSolAugustus.DATA.Series);

    public MoltSolAugustus_ImperialArchers()
    {
        super(DATA);

        Initialize(0, 2, 1, 2);
        SetUpgrade(0, 2, 1, 0);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        for (int i = 0; i < 3; i++)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(secondaryValue, DamageInfo.DamageType.THORNS, AttackEffects.NONE)
            .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowDagger(c.hb, 0.25f).SetColor(Color.TAN)).duration * 0f)
            .SetDuration(0.05f, false);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.StackPower(new SupportDamagePower(p, magicNumber));
    }
}