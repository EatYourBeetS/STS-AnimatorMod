package eatyourbeets.cards.animatorClassic.series.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animatorClassic.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class YuiTsuruno extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(YuiTsuruno.class).SetSeriesFromClassPackage().SetAttack(0, CardRarity.COMMON, EYBAttackType.Elemental);
    static
    {
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public YuiTsuruno()
    {
        super(DATA);

        Initialize(5, 0);
        SetUpgrade(3, 0);


        SetSpellcaster();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ChannelOrb(new Fire());
        GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.ChannelOrb(new Fire());
        GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, 1)
        .ShowEffect(true, true)
        .SetOrigin(CardSelection.Bottom);
    }
}