package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animator.special.Oktavia;
import eatyourbeets.cards.animator.status.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SayakaMiki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SayakaMiki.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Oktavia(), true);
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public SayakaMiki()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.ChannelOrb(new Frost(), true);

        IntellectPower.PreserveOnce();

        AbstractCard last = GameUtilities.GetLastCardPlayed(true, 1);
        if (HasSynergy() && last != null && last.cardID.equals(Curse_GriefSeed.DATA.ID))
        {
            GameActions.Bottom.MakeCardInDiscardPile(new Oktavia()).SetUpgrade(upgraded, false);
            GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
            GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
        }
    }
}
