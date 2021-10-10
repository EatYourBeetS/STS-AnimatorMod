package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.cards.animator.curse.MadokaKaname_Krimheild;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.utilities.*;

public class MadokaKaname extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new MadokaKaname_Krimheild(), true);
            });

    private static final int HEAL_AMOUNT = 4;

    public MadokaKaname()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);

        SetHealing(true);
        SetExhaust(true);
        SetProtagonist(true);
        SetHarmonic(true);

        SetAffinityRequirement(Affinity.Light, 3);
        SetAffinityRequirement(Affinity.Dark, 3);

        SetSoul(4, 0, MadokaKaname_Krimheild::new);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return secondaryValue <= 0 ? null : HPAttribute.Instance.SetCard(this, false).SetText(new ColoredString(secondaryValue, Colors.Cream(1f)));
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.ModifySecondaryValue(this,
        JUtils.Count(player.drawPile.group, c -> c.type == CardType.CURSE) +
        JUtils.Count(player.discardPile.group, c -> c.type == CardType.CURSE) +
        JUtils.Count(player.hand.group, c -> c.type == CardType.CURSE), false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.PurgeFromPile(name, magicNumber, p.exhaustPile)
        .ShowEffect(true, true)
        .SetOptions(true, true)
        .SetFilter(c -> CardType.CURSE.equals(c.type))
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.HealPlayerLimited(this, HEAL_AMOUNT * cards.size());
                if (CheckAffinity(Affinity.Light) && CheckAffinity(Affinity.Dark) && info.TryActivateLimited()) {
                    GameActions.Bottom.GainSupportDamage(HEAL_AMOUNT * cards.size());
                }
                GameActions.Bottom.VFX(new BorderFlashEffect(Color.PINK, true));
            }
        });

        cooldown.ProgressCooldownAndTrigger(m);
    }
}
