package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.Miracle;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animator.HinaKagiyamaPower;
import eatyourbeets.utilities.GameActions;

public class HinaKagiyama extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HinaKagiyama.class)
            .SetPower(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TouhouProject)
            .SetMultiformData(2)
            .PostInitialize(data -> data.AddPreview(new Miracle(), false));

    public HinaKagiyama()
    {
        super(DATA);

        Initialize(0, 0, HinaKagiyamaPower.CARD_DRAW_AMOUNT);

        SetAffinity_Blue(1);
        SetAffinity_Light(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HinaKagiyamaPower(p, 1));
    }

    @Override
    protected void OnUpgrade()
    {
        if (auxiliaryData.form == 0) {
            SetInnate(true);
        }
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (timesUpgraded > 0) {
            SetInnate(form == 0);
        }
        return super.SetForm(form, timesUpgraded);
    };
}