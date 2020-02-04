package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.ui.cards.TargetEffectPreview;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mitsurugi extends AnimatorCard
{
    public static final String ID = Register_Old(Mitsurugi.class);

    private final TargetEffectPreview targetEffectPreview = new TargetEffectPreview(this::OnTargetChanged);
    private boolean showDamage = true;

    public Mitsurugi()
    {
        super(ID, 0, CardRarity.COMMON, EYBAttackType.Normal);

        Initialize(8, 0, 1, 4);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.Konosuba);
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
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainForce(magicNumber);

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