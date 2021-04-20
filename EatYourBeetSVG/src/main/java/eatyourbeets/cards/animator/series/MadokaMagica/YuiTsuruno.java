package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class YuiTsuruno extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuiTsuruno.class).SetAttack(0, CardRarity.COMMON, EYBAttackType.Elemental);
    static
    {
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public YuiTsuruno()
    {
        super(DATA);

        Initialize(5, 0);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.MadokaMagica);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, 1)
        .ShowEffect(true, true)
        .SetOrigin(CardSelection.Bottom);
    }
}