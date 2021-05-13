package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.InverseTohka;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class TohkaYatogami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TohkaYatogami.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Normal);

    private boolean transformed;

    static
    {
        DATA.AddPreview(new InverseTohka(), true);
    }

    public TohkaYatogami()
    {
        super(DATA);

        Initialize(10, 0, 20);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void update()
    {
        super.update();

        if (AbstractDungeon.player != null && !transformed && AbstractDungeon.player.exhaustPile.size() >= magicNumber)
        {
            transformed = true;
            GameActions.Last.ReplaceCard(uuid, new InverseTohka()).SetUpgrade(upgraded);
        }
    }

    @Override
    protected void UpdateDamage(float amount)
    {
        super.UpdateDamage(baseDamage);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }
}