package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.animatorClassic.HinaKagiyamaPower;
import eatyourbeets.utilities.GameActions;

public class HinaKagiyama extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(HinaKagiyama.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new HinaKagiyama_Miracle(), false);
    }

    public HinaKagiyama()
    {
        super(DATA);

        Initialize(0, 0, HinaKagiyamaPower.CARD_DRAW_AMOUNT);

        SetSeries(CardSeries.TouhouProject);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new HinaKagiyamaPower(p, 1));
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }
}