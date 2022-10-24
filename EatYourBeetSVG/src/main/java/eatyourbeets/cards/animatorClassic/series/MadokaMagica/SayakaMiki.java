package eatyourbeets.cards.animatorClassic.series.MadokaMagica;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.animatorClassic.curse.Curse_GriefSeed;
import eatyourbeets.cards.animatorClassic.special.SayakaMiki_Oktavia;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SayakaMiki extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(SayakaMiki.class).SetSeriesFromClassPackage().SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new SayakaMiki_Oktavia(), true);
        DATA.AddPreview(new Curse_GriefSeed(), false);
    }

    public SayakaMiki()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.ChannelOrb(new Frost());

        GameUtilities.RetainPower(Affinity.Blue);

        AbstractCard last = GameUtilities.GetLastCardPlayed(true, 1);
        if (HasSynergy() && last != null && last.cardID.equals(Curse_GriefSeed.DATA.ID))
        {
            GameActions.Bottom.MakeCardInDiscardPile(new SayakaMiki_Oktavia()).SetUpgrade(upgraded, false);
            GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
            GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
        }
    }
}
