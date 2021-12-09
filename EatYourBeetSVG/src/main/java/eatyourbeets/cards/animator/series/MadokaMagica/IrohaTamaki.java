package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.IrohaTamaki_Giovanna;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IrohaTamaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IrohaTamaki.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new IrohaTamaki_Giovanna(), false));

    public IrohaTamaki()
    {
        super(DATA);

        Initialize(3, 5, 2, 2);
        SetUpgrade(0, 2, 1 ,1);

        SetAffinity_Blue(1);
        SetAffinity_Light(1, 0 ,1);

        SetAffinityRequirement(Affinity.Light, 3);

        SetSoul(4, 0, IrohaTamaki_Giovanna::new);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_VERTICAL);

        if (info.IsSynergizing)
        {
            GameActions.Bottom.Scry(magicNumber).AddCallback(cards -> {
                for (AbstractCard c : cards) {
                    if (GameUtilities.IsHindrance(c)) {
                        GameActions.Bottom.PlayCopy(c, m);
                        GameActions.Last.Purge(c).ShowEffect(true);
                    }
                }
            });
        }
    }
}