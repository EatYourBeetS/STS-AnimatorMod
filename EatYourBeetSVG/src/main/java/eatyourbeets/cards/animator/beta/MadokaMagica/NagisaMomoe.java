package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.pileSelection.FetchFromPile;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class NagisaMomoe extends AnimatorCard {
    public static final EYBCardData DATA = Register(NagisaMomoe.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public NagisaMomoe() {
        super(DATA);

        Initialize(0, 0, 1);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        FetchFromPile fetchFromPile = new FetchFromPile(name, magicNumber, p.discardPile);

        GameActions.Top.Add(fetchFromPile.SetOptions(false, false));

        GameActions.Bottom.MakeCardInDiscardPile(new Charlotte()).SetOptions(upgraded, false);
        GameActions.Bottom.MakeCardInDiscardPile(new Curse_GriefSeed());
        this.exhaustOnUseOnce = true;
    }
}