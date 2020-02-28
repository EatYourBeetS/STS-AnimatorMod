package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.YachiyoNanamiPower;
import eatyourbeets.utilities.GameActions;

public class YachiyoNanami extends AnimatorCard {
    public static final EYBCardData DATA = Register(YachiyoNanami.class).SetPower(2, CardRarity.COMMON);

    public YachiyoNanami() {
        super(DATA);

        Initialize(0, 0, 4);
        SetUpgrade(0, 0, 1);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.StackPower(new YachiyoNanamiPower(p, magicNumber));
    }
}