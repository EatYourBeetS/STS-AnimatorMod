package eatyourbeets.cards.animatorClassic.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mitsurugi extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Mitsurugi.class).SetAttack(0, CardRarity.COMMON);

    private final TargetEffectPreview targetEffectPreview = new TargetEffectPreview(this::OnTargetChanged);
    private boolean showDamage = true;

    public Mitsurugi()
    {
        super(DATA);

        Initialize(8, 0, 1, 4);
        SetUpgrade(2, 0, 0, 0);
        SetScaling(0, 0, 1);

        SetSeries(CardSeries.Konosuba);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        if (showDamage)
        {
            return super.GetDamageInfo();
        }

        return null;
    }

    @Override
    public void update()
    {
        super.update();

        targetEffectPreview.Update();
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.GainBlock(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainForce(magicNumber, upgraded);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
    }

    private void OnTargetChanged(AbstractMonster monster)
    {
        showDamage = (monster == null || !GameUtilities.IsAttacking(monster.intent));
    }
}