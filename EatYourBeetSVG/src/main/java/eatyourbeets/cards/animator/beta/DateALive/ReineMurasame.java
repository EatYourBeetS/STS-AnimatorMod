package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ReineMurasame extends AnimatorCard implements Hidden {
    public static final EYBCardData DATA = Register(ReineMurasame.class).SetSkill(-1, CardRarity.COMMON, EYBCardTarget.None);

    static
    {
        DATA.AddPreview(new ShidoItsuka(), true);
    }

    public ReineMurasame() {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int stacks = GameUtilities.UseXCostEnergy(this);
        if (stacks > 0)
        {

        }
    }
}