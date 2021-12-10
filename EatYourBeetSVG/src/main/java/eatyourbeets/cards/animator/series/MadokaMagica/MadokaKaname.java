package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.animator.special.MadokaKaname_Krimheild;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class MadokaKaname extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new MadokaKaname_Krimheild(), true);
            });

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 3, 4);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);

        SetHealing(true);
        SetExhaust(true);
        SetProtagonist(true);
        SetHarmonic(true);

        SetSoul(2, 0, MadokaKaname_Krimheild::new);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return secondaryValue <= 0 ? null : HPAttribute.Instance.SetCard(this, false).SetText(new ColoredString(secondaryValue, Colors.Cream(1f)));
    }

    @Override
    public int GetXValue() {
        return secondaryValue * (JUtils.Count(player.drawPile.group, c -> c.type == CardType.CURSE) +
                JUtils.Count(player.discardPile.group, c -> c.type == CardType.CURSE) +
                JUtils.Count(player.hand.group, c -> c.type == CardType.CURSE) +
                JUtils.Count(player.exhaustPile.group, c -> c.type == CardType.CURSE));
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.RecoverHP(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.PurgeFromPile(name, magicNumber, p.exhaustPile, player.hand, player.discardPile, player.drawPile)
        .ShowEffect(true, true)
        .SetOptions(true, true)
        .SetFilter(c -> CardType.CURSE.equals(c.type))
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.HealPlayerLimited(this, secondaryValue * cards.size());
                GameActions.Bottom.VFX(new BorderFlashEffect(Color.PINK, true));
            }
        });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}
